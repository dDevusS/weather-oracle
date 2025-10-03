package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.configuration.application.OpenWeatherApiProperties;
import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationDto;
import com.ddevuss.weather.oracle.dto.externalApi.ForecastApiResponseDto;
import com.ddevuss.weather.oracle.mapper.ForecastMapper;
import com.ddevuss.weather.oracle.utils.LocationDeduplicator;
import com.ddevuss.weather.oracle.utils.OpenWeatherUrlBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
public class OpenWeatherService implements IOpenWeatherService {

    private final RestClient openWeatherRestClient;
    private final OpenWeatherApiProperties apiProperties;

    @Override
    public List<LocationDto> searchLocationByName(String locationName) {
        locationName = locationName.trim();
        String uri = OpenWeatherUrlBuilder.buildUrlForGeoApi(locationName, apiProperties.key());

        var locations = openWeatherRestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(LocationDto[].class);

        return LocationDeduplicator.deduplicate(locations);
    }

    @Override
    public List<ForecastDto> getWeatherForecast(List<LocationDto> locations) {
        List<ForecastDto> forecasts = new ArrayList<>();

        for (LocationDto location : locations) {
            String uri = OpenWeatherUrlBuilder.buildUrlForWeatherApi(location.getLat(), location.getLon(), apiProperties.key());

            var responseEntity = openWeatherRestClient.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(s -> s.isSameCodeAs(NOT_FOUND), (req, res) -> {
                        log.warn("OpenWeather: 404 for location id={} lat={}, lon={}",
                                location.getId(), location.getLat(), location.getLon());
                    })
                    .toEntity(ForecastApiResponseDto.class);

            if (responseEntity.getStatusCode().isError() || responseEntity.getBody() == null) {
                continue;
            }
            forecasts.add(ForecastMapper.fromResponse(responseEntity.getBody(), location));
        }

        return forecasts;
    }

}
