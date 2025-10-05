package com.ddevuss.weather.oracle.forecast.domain;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record Forecast(Long locationId, String locationName, String countryCode, String state, String iconUrl,
                       String description, Float temperature, Integer pressure,
                       Integer humidity) implements Serializable {

}