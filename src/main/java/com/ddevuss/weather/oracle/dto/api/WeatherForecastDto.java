package com.ddevuss.weather.oracle.dto.api;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class WeatherForecastDto implements Serializable {

    Long locationId;
    String locationName;
    String countryCode;
    String description;
    Float temperature;
    Float feelsLikeTemperature;
    Integer pressure;
    Integer humidity;

}
