package com.ddevuss.weather.oracle.configuration.application;

import com.ddevuss.weather.oracle.configuration.application.model.JwtConfig;
import com.ddevuss.weather.oracle.configuration.application.model.TimeOfLife;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.ddevuss.weather.oracle.configuration.application.ConfigurationConstants.BYTES_FOR_JWT_SECRET;

@Slf4j
@Configuration
public class WeatherOracleConfiguration {

    @Profile("!test")
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

    @Bean
    JwtConfig jwtConfig(JwtProperties jwtProperties, TokenExpirationProperties tokenExpirationProperties) {
        return new JwtConfig(normalizeJwtSecret(jwtProperties.secret()),
                jwtProperties.secure(),
                new TimeOfLife(tokenExpirationProperties.accessToken(), tokenExpirationProperties.refreshToken()));
    }

    private static String normalizeJwtSecret(String secret) {
        if (isValidSecret(secret)) {
            return secret;
        }
        log.warn("JWT secret is not set or invalid, generating new one");

        return generateJwtSecret();
    }

    private static boolean isValidSecret(String secret) {
        return !(secret == null || secret.isBlank() || secret.length() < BYTES_FOR_JWT_SECRET);
    }

    private static String generateJwtSecret() {
        byte[] buf = new byte[BYTES_FOR_JWT_SECRET];
        new java.security.SecureRandom().nextBytes(buf);
        return java.util.Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(buf);
    }

}
