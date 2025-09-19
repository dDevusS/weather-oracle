package com.ddevuss.weather.oracle.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Slf4j
@ConfigurationProperties(prefix = "application", ignoreInvalidFields = true)
@Data
public class WeatherOracleConfiguration {

    private static final int BYTES_FOR_JWT_SECRET = 32;
    private static final long DEFAULT_ACCESS_EXPIRATION = 5;
    private static final long DEFAULT_REFRESH_EXPIRATION = 3;
    private static final List<String> DEFAULT_ORIGIN_PATTERNS = List.of("http://localhost:*");
    private static final List<String> DEFAULT_ALLOWED_ORIGINS = List.of("http://localhost:4020");
    private static final String DEFAULT_OPEN_WEATHER_API_URL = "https://api.openweathermap.org";

    private OpenWeatherApi openWeatherApi;

    private Cors cors;

    private Jwt jwt;

    @PostConstruct
    void finalizeConfiguration() {
        log.info("----------------------------------------------");
        log.info("Using JWT for authentication and authorization");

        if (jwt == null) jwt = new Jwt(null, null, null);
        if (openWeatherApi == null) openWeatherApi = new OpenWeatherApi(null, null);
        if (cors == null) cors = new Cors(null, null);

        if (!jwt.secure) {
            log.warn("Using insecure http cookie for refresh token");
        }
        else {
            log.info("Using secure http cookie for refresh token");
        }

        log.info("Duration of access tokens: {} minutes", jwt.timeOfLife.accessToken.toMinutes());
        log.info("Duration of refresh tokens: {} days", jwt.timeOfLife.refreshToken.toDays());
        log.info("----------------------------------------------");
    }

    public record OpenWeatherApi(@DefaultValue("") String key,
                                 @DefaultValue(DEFAULT_OPEN_WEATHER_API_URL) String url) {

        public OpenWeatherApi {
            if (url == null || url.isBlank()) {
                url = DEFAULT_OPEN_WEATHER_API_URL;
            }
            if (key == null || key.isBlank()) {
                log.error("Open Weather API key is required. Please set it in application.properties file.");
                System.exit(1);
            }
        }
    }

    public record Cors(List<String> allowedOriginPatterns,
                       List<String> allowedOrigins) {

        public Cors {
            if (allowedOriginPatterns == null || allowedOriginPatterns.isEmpty()) {
                allowedOriginPatterns = DEFAULT_ORIGIN_PATTERNS;
            }
            if (allowedOrigins == null || allowedOrigins.isEmpty()) {
                allowedOrigins = DEFAULT_ALLOWED_ORIGINS;
            }
        }
    }

    public record Jwt(String secret, Boolean secure, TimeOfLife timeOfLife) {

        public Jwt {
            if (secret == null || secret.isBlank() || secret.length() < BYTES_FOR_JWT_SECRET) {
                log.warn("JWT secret is not set or invalid, generating new one");
                secret = generateJwtSecret();
            }
            if (secure == null) {
                secure = true;
            }
            if (timeOfLife == null) {
                timeOfLife = new TimeOfLife(null, null);
            }
        }

        private String generateJwtSecret() {
            byte[] buf = new byte[BYTES_FOR_JWT_SECRET];
            new java.security.SecureRandom().nextBytes(buf);
            return java.util.Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(buf);
        }
    }

    public record TimeOfLife(@DurationUnit(ChronoUnit.MINUTES) Duration accessToken,
                             @DurationUnit(ChronoUnit.DAYS) Duration refreshToken) {

        public TimeOfLife {
            if (accessToken == null) accessToken = Duration.ofMinutes(DEFAULT_ACCESS_EXPIRATION);
            if (refreshToken == null) refreshToken = Duration.ofDays(DEFAULT_REFRESH_EXPIRATION);
        }
    }

}
