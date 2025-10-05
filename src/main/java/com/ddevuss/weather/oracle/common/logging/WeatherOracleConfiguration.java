package com.ddevuss.weather.oracle.common.logging;

import com.ddevuss.weather.oracle.auth.domain.jwt.configuration.SchedulerProperties;
import com.ddevuss.weather.oracle.auth.domain.jwt.configuration.model.JwtConfig;
import com.ddevuss.weather.oracle.common.client.config.OpenWeatherApiProperties;
import com.ddevuss.weather.oracle.security.CorsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class WeatherOracleConfiguration {

    @Profile(value = {"!test"})
    @Bean
    CommandLineRunner logConfiguration(OpenWeatherApiProperties openWeatherApiProperties,
                                       JwtConfig jwtConfig,
                                       CorsProperties corsProperties,
                                       SchedulerProperties schedulerProperties) {
        return args -> {
            log.info("----------------------------------------------");
            log.debug("Open weather API url: {} ", openWeatherApiProperties.url());
            log.info("Using JWT for authentication and authorization");
            if (jwtConfig.isSecure()) {
                log.info("Using secure http cookie for refresh token");
            }
            else {
                log.warn("Using insecure http cookie for refresh token");
            }
            log.debug("Allow origins: {} ", String.join(", ", corsProperties.allowedOrigins()));
            log.debug("Allow origins patterns: {} ", String.join(", ", corsProperties.allowedOriginPatterns()));
            log.debug("Setup a schedule for cleanup of expired jwt tokens: {} with time zone {}", schedulerProperties.schedule(), schedulerProperties.zone());
            log.info("Duration of access tokens: {} minutes", jwtConfig.timeOfLife().accessToken().toMinutes());
            log.info("Duration of refresh tokens: {} days", jwtConfig.timeOfLife().refreshToken().toDays());
            log.info("----------------------------------------------");
        };
    }

}
