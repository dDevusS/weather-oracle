package com.ddevuss.weather.oracle.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Slf4j
@ConfigurationProperties(prefix = "application")
@Data
public class WeatherOracleConfiguration {

    private static final int BITES_FOR_JWT_SECRET = 32;
    private static final Duration DEFAULT_ACCESS_TTL = Duration.ofMinutes(5);
    private static final Duration DEFAULT_REFRESH_TTL = Duration.ofDays(3);

    private OpenWeatherApi openWeatherApi = new OpenWeatherApi("", "https://api.openweathermap.org");

    private Cors cors = new Cors(List.of("http://localhost:*"), List.of("http://localhost:4020"));

    private Jwt jwt = new Jwt("", false, new TimeOfLife(DEFAULT_ACCESS_TTL, DEFAULT_REFRESH_TTL));

    @PostConstruct
    void finalizeConfiguration() {
        this.jwt = normalizeJwt(this.jwt);

        if (openWeatherApi.key().isBlank()) {
            log.error("Open Weather API key is required. Please set it in application.properties file.");
            System.exit(1);
        }
    }

    public void setJwt(Jwt in) {
        TimeOfLife tol = in.timeOfLife() != null ? in.timeOfLife()
                : this.jwt.timeOfLife();

        log.info("----------------------------------------------");
        log.info("Using JWT for authentication and authorization");
        log.info("Duration of access tokens: {} minutes", tol.accessToken().toMinutes());
        log.info("Duration of refresh tokens: {} days", tol.refreshToken().toDays());
        if (in.secure()) {
            log.info("Using secure http cookie for refresh token");
        }

        this.jwt = normalizeJwt(new Jwt(in.secret(), in.secure(), tol));
    }

    private Jwt normalizeJwt(Jwt jwt) {
        String secret = jwt.secret();
        if (secret == null || secret.isBlank() || secret.length() < BITES_FOR_JWT_SECRET) {
            log.warn("JWT secret is not set or invalid, generating new one");
            secret = generateJwtSecret();
        }

        TimeOfLife tol = jwt.timeOfLife() != null ? jwt.timeOfLife()
                : new TimeOfLife(DEFAULT_ACCESS_TTL, DEFAULT_REFRESH_TTL);

        return new Jwt(secret, jwt.secure(), tol);
    }

    private String generateJwtSecret() {
        byte[] buf = new byte[BITES_FOR_JWT_SECRET];
        new java.security.SecureRandom().nextBytes(buf);
        return java.util.Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(buf);
    }

    public record OpenWeatherApi(String key, String url) {

    }

    public record Cors(List<String> allowedOriginPatterns, List<String> allowedOrigins) {

    }

    public record Jwt(String secret, boolean secure, TimeOfLife timeOfLife) {

    }

    public record TimeOfLife(@DurationUnit(ChronoUnit.MINUTES) Duration accessToken,
                             @DurationUnit(ChronoUnit.DAYS) Duration refreshToken) {

    }
}
