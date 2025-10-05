package com.ddevuss.weather.oracle.forecast.infra.client.logging;

import com.ddevuss.weather.oracle.forecast.infra.client.ForecastClient;
import com.ddevuss.weather.oracle.forecast.domain.Forecast;
import com.ddevuss.weather.oracle.location.dto.LocationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Primary
@Service
class OpenWeatherLoggingDecorator implements ForecastClient {

    private final ForecastClient target;

    private static final String MESSAGE_SUCCESS_TEMPLATE = "Request to Open Weather API for {} method has been processed successfully";
    private static final String MESSAGE_OW_REQUEST_TEMPLATE = "Sending requests to Open Weather API to get weather forecast for {} locations";

    private static final String METHOD_NAME_GET_WEATHER_FORECAST = "getWeatherForecast";

    public OpenWeatherLoggingDecorator(@Qualifier("forecastClientImp") ForecastClient target) {
        this.target = target;
    }

    @Override
    public List<Forecast> getWeatherForecast(List<LocationDto> locations) {
        log.debug(MESSAGE_OW_REQUEST_TEMPLATE, locations.size());
        var forecasts = target.getWeatherForecast(locations);
        log.debug(MESSAGE_SUCCESS_TEMPLATE, METHOD_NAME_GET_WEATHER_FORECAST);
        return forecasts;

    }

}
