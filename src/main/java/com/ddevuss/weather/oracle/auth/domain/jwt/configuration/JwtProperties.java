package com.ddevuss.weather.oracle.auth.domain.jwt.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "application.jwt",  ignoreUnknownFields = true)
public record JwtProperties(String secret,
                            @DefaultValue("true") boolean secure) {

}
