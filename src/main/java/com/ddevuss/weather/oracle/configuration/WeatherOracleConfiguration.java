package com.ddevuss.weather.oracle.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "open.weather.api")
@Data
public class WeatherOracleConfiguration {

    private String key;
    private String url;
    private RefreshTokenProperties refreshToken;

    @Bean
    @ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.jwt")
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Data
    public static class JwtProperties {
        private String secret;
    }

    @Data
    public static class RefreshTokenProperties {
        private String secretForHashing;
    }
}
