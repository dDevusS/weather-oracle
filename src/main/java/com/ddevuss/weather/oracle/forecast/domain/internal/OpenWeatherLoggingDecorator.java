package com.ddevuss.weather.oracle.forecast.domain.internal;

import com.ddevuss.weather.oracle.forecast.domain.OpenWeatherService;
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
class OpenWeatherLoggingDecorator implements OpenWeatherService {

    private final OpenWeatherService target;

    private static final String MESSAGE_SUCCESS_TEMPLATE = "Request to Open Weather API for {} method has been processed successfully";
    private static final String MESSAGE_GEO_REQUEST_TEMPLATE = "Sending request to Open Weather API for searching location with name {}";
    private static final String MESSAGE_OW_REQUEST_TEMPLATE = "Sending requests to Open Weather API to get weather forecast for {} locations";

    public OpenWeatherLoggingDecorator(@Qualifier("openWeatherServiceImp") OpenWeatherService target) {
        this.target = target;
    }

    @Override
    public List<LocationDto> searchLocationByName(String locationName) {
        log.debug(MESSAGE_GEO_REQUEST_TEMPLATE, locationName);
        var locations = target.searchLocationByName(locationName);
        log.debug(MESSAGE_SUCCESS_TEMPLATE, "searchLocationByName");
        return locations;

    }

    @Override
    public List<ForecastDto> getWeatherForecast(List<LocationDto> locations) {
        log.debug(MESSAGE_OW_REQUEST_TEMPLATE, locations.size());
        var forecasts = target.getWeatherForecast(locations);
        log.debug(MESSAGE_SUCCESS_TEMPLATE, "getWeatherForecast");
        return forecasts;

    }

}
