package com.ddevuss.weather.oracle.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class ForecastDto implements Serializable {

    Long locationId;
    String locationName;
    String countryCode;
    String state;
    String iconUrl;
    String description;
    Float temperature;
    Integer pressure;
    Integer humidity;

}
