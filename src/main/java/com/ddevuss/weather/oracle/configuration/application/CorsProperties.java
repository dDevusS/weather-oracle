package com.ddevuss.weather.oracle.configuration.application;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "application.cors", ignoreUnknownFields = true)
public record CorsProperties(@DefaultValue("http://localhost:*") @NotEmpty List<String> allowedOriginPatterns,
                             @DefaultValue("http://localhost:4020") @NotEmpty List<String> allowedOrigins) {

}
