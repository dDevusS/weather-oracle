package com.ddevuss.weather.oracle.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class WeatherOracleConfiguration {

    private OpenWeatherApi openWeatherApi;
    private Cors cors;
    private Jwt jwt;

    public record OpenWeatherApi(String key, String url) {

    }

    public record Cors(List<String> allowedOriginPatterns, List<String> allowedOrigins) {

    }

    public record Jwt(String secret) {

    }
}
