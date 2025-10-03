package com.ddevuss.weather.oracle.forecast.client.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import static com.ddevuss.weather.oracle.forecast.client.OpenWeatherConstants.DEFAULT_OPEN_WEATHER_API_URL;

@Validated
@ConfigurationProperties(prefix = "application.open-weather-api", ignoreInvalidFields = true)
public record OpenWeatherApiProperties(@NotBlank(message = "{api.key.required}") String key,
                                       @DefaultValue(DEFAULT_OPEN_WEATHER_API_URL) String url) {
}
