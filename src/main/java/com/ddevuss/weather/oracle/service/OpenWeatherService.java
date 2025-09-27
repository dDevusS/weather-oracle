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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class OpenWeatherService {

    private final RestClient openWeatherRestClient;
    private final OpenWeatherApiProperties apiProperties;

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

    public List<ForecastDto> getWeatherForecast(List<LocationDto> locations) {
        List<ForecastDto> forecasts = new ArrayList<>();

        for (LocationDto location : locations) {
            String uri = OpenWeatherUrlBuilder.buildUrlForWeatherApi(location.getLat(), location.getLon(), apiProperties.key());

            var responseEntity = openWeatherRestClient.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(s -> s.value() == 404, (req, res) -> {
                        log.info("OpenWeather: 404 for location id={} lat={}, lon={}",
                                location.getId(), location.getLat(), location.getLon());
                    })
                    .toEntity(ForecastApiResponseDto.class);

            if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
                continue;
            }
            forecasts.add(ForecastMapper.fromResponse(responseEntity.getBody(), location));
        }

        return forecasts;
    }

}
