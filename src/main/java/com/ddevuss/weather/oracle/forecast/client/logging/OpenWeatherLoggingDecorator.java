package com.ddevuss.weather.oracle.forecast.client.logging;

import com.ddevuss.weather.oracle.forecast.client.OpenWeatherClient;
import com.ddevuss.weather.oracle.forecast.dto.ForecastDto;
import com.ddevuss.weather.oracle.location.dto.LocationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Primary
@Service
class OpenWeatherLoggingDecorator implements OpenWeatherClient {

    private final OpenWeatherClient target;

    private static final String MESSAGE_SUCCESS_TEMPLATE = "Request to Open Weather API for {} method has been processed successfully";
    private static final String MESSAGE_GEO_REQUEST_TEMPLATE = "Sending request to Open Weather API for searching location with name {}";
    private static final String MESSAGE_OW_REQUEST_TEMPLATE = "Sending requests to Open Weather API to get weather forecast for {} locations";

    private static final String METHOD_NAME_SEARCH_LOCATION_BY_NAME = "searchLocationByName";
    private static final String METHOD_NAME_GET_WEATHER_FORECAST = "getWeatherForecast";

    public OpenWeatherLoggingDecorator(@Qualifier("openWeatherClientImp") OpenWeatherClient target) {
        this.target = target;
    }

    @Override
    public List<LocationDto> searchLocationByName(String locationName) {
        log.debug(MESSAGE_GEO_REQUEST_TEMPLATE, locationName);
        var locations = target.searchLocationByName(locationName);
        log.debug(MESSAGE_SUCCESS_TEMPLATE, METHOD_NAME_SEARCH_LOCATION_BY_NAME);
        return locations;

    }

    @Override
    public List<ForecastDto> getWeatherForecast(List<LocationDto> locations) {
        log.debug(MESSAGE_OW_REQUEST_TEMPLATE, locations.size());
        var forecasts = target.getWeatherForecast(locations);
        log.debug(MESSAGE_SUCCESS_TEMPLATE, METHOD_NAME_GET_WEATHER_FORECAST);
        return forecasts;

    }

}
