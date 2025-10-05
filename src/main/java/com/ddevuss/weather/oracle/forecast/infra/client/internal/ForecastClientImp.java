package com.ddevuss.weather.oracle.forecast.infra.client.internal;

import com.ddevuss.weather.oracle.common.client.config.OpenWeatherApiProperties;
import com.ddevuss.weather.oracle.common.util.OpenWeatherUrlBuilder;
import com.ddevuss.weather.oracle.forecast.infra.client.ForecastClient;
import com.ddevuss.weather.oracle.forecast.domain.Forecast;
import com.ddevuss.weather.oracle.location.domain.LocationDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
class ForecastClientImp implements ForecastClient {

    private final RestClient openWeatherRestClient;
    private final OpenWeatherApiProperties apiProperties;

    @Override
    public List<Forecast> getWeatherForecast(List<LocationDto> locations) {
        List<Forecast> forecasts = new ArrayList<>();

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
                    .toEntity(OpenWeatherExternalDto.class);

            if (responseEntity.getStatusCode().isError() || responseEntity.getBody() == null) {
                continue;
            }
            forecasts.add(ForecastMapper.fromResponse(responseEntity.getBody(), location));
        }

        return forecasts;
    }

}
