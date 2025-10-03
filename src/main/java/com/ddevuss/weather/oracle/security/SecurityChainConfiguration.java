package com.ddevuss.weather.oracle.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityChainConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationEntryPoint restEntryPoint,
                                                   AccessDeniedHandler accessDeniedHandler) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/auth/registration",
                                "/api/auth/refresh",
                                "/swagger-ui/**",
                                "/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                                .authenticationEntryPoint(restEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

}
