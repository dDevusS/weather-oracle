package com.ddevuss.weather.oracle.forecast;

import com.ddevuss.weather.oracle.location.LocationDto;

import java.util.List;

public interface IOpenWeatherService {

    List<LocationDto> searchLocationByName(String locationName);

    List<ForecastDto> getWeatherForecast(List<LocationDto> locations);
}
