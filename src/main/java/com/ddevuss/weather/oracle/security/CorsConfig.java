package com.ddevuss.weather.oracle.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsProperties properties) {
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowedOrigins(properties.allowedOrigins());
        config.setAllowedOriginPatterns(properties.allowedOriginPatterns());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
