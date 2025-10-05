package com.ddevuss.weather.oracle.forecast.infra.client;

import com.ddevuss.weather.oracle.forecast.domain.Forecast;
import com.ddevuss.weather.oracle.location.dto.LocationDto;

import java.util.List;

public interface ForecastClient {

    List<Forecast> getWeatherForecast(List<LocationDto> locations);
}
