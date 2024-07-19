package com.ddevuss.weather.oracle.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainInfo {

        Float temp;

        @JsonProperty("feels-like")
        Float feelsLike;

        Integer pressure;

        Integer humidity;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SysInfo {

        String country;
    }
}
