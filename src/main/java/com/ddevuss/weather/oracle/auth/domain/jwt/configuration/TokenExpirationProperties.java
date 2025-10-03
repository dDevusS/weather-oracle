package com.ddevuss.weather.oracle.auth.domain.jwt.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.ddevuss.weather.oracle.auth.domain.jwt.JwtConstants.*;

@ConfigurationProperties(prefix = "application.jwt.time-of-life",  ignoreUnknownFields = true)
public record TokenExpirationProperties(@DefaultValue(DEFAULT_ACCESS_EXPIRATION) @DurationUnit(ChronoUnit.MINUTES) Duration accessToken,
                                        @DefaultValue(DEFAULT_REFRESH_EXPIRATION) @DurationUnit(ChronoUnit.DAYS) Duration refreshToken) {

}
