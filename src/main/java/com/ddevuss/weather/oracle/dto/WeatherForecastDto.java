package com.ddevuss.weather.oracle.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class WeatherForecastDto implements Serializable {

    String locationName;
    String countryCode;
    String description;
    Float temperature;
    Float feelsLikeTemperature;
    Integer pressure;
    Integer humidity;

}
