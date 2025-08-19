package com.ddevuss.weather.oracle.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "open.weather.api")
@Data
public class WeatherOracleConfiguration {

    private String key;
    private String url;
    private Jwt jwt;

    public record Jwt(String secret) {}
}
