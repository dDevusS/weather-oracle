package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationDto;

import java.util.List;

public interface IOpenWeatherService {

    List<LocationDto> searchLocationByName(String locationName);

    List<ForecastDto> getWeatherForecast(List<LocationDto> locations);
}
