package com.ddevuss.weather.oracle.forecast.api;

import com.ddevuss.weather.oracle.forecast.dto.ForecastDto;
import com.ddevuss.weather.oracle.location.LocationDto;

import java.util.List;

public interface OpenWeatherService {

    List<LocationDto> searchLocationByName(String locationName);

    List<ForecastDto> getWeatherForecast(List<LocationDto> locations);
}
