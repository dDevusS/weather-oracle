package com.ddevuss.weather.oracle.forecast.domain;

import com.ddevuss.weather.oracle.location.dto.LocationDto;

import java.util.List;

public interface ForecastRepository {

    List<Forecast> getForecasts(List<LocationDto> locations);
}
