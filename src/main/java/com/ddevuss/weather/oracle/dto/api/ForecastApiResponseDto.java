package com.ddevuss.weather.oracle.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ForecastApiResponseDto {

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
