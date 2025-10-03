package com.ddevuss.weather.oracle.forecast.client.internal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
class OpenWeatherExternalDto {

    WeatherDescription[] weather;

    MainInfo main;

    SysInfo sys;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherDescription {

        String description;

        String icon;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainInfo {

        Float temp;

        Integer pressure;

        Integer humidity;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SysInfo {

        String country;
    }
}
