package com.ddevuss.weather.oracle.forecast.client;

import com.ddevuss.weather.oracle.forecast.dto.ForecastDto;
import com.ddevuss.weather.oracle.location.dto.LocationDto;

import java.util.List;

public interface OpenWeatherClient {

    List<LocationDto> searchLocationByName(String locationName);

    List<ForecastDto> getWeatherForecast(List<LocationDto> locations);
}
