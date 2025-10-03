package com.ddevuss.weather.oracle.configuration;

import com.ddevuss.weather.oracle.configuration.application.CorsProperties;
import com.ddevuss.weather.oracle.configuration.application.model.JwtConfig;
import com.ddevuss.weather.oracle.security.jwt.TokenType;
import com.ddevuss.weather.oracle.utils.ProblemDetailBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

import static com.ddevuss.weather.oracle.security.Constants.ALGORITHM;
import static com.ddevuss.weather.oracle.security.jwt.JwtClaims.TYPE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

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

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsProperties properties) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(properties.allowedOrigins());
        config.setAllowedOriginPatterns(properties.allowedOriginPatterns());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public AuthenticationEntryPoint restEntryPoint(ObjectMapper mapper) {
        return (request, response, exception) -> {
            response.setStatus(UNAUTHORIZED.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            ProblemDetail pd = ProblemDetailBuilder.forStatus(UNAUTHORIZED)
                    .title("Credentials failed")
                    .uri(request)
                    .build();

            mapper.writeValue(response.getOutputStream(), pd);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper mapper) {
        return (request, response, exception) -> {
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            ProblemDetail pd = ProblemDetailBuilder.forStatus(FORBIDDEN)
                    .title("Access denied")
                    .uri(request)
                    .build();

            mapper.writeValue(response.getOutputStream(), pd);
        };
    }

    @Bean
    public JwtDecoder jwtDecoder(JwtConfig properties) {
        SecretKey key = new SecretKeySpec(properties.secret().getBytes(), ALGORITHM);
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String type = jwt.getClaimAsString(TYPE);

            if (!TokenType.ACCESS_TOKEN.getCode().equals(type)) {
                throw new JwtException("Invalid token type: " + type);
            }

            MDC.put("username", jwt.getClaimAsString("sub"));

            return AuthorityUtils.NO_AUTHORITIES;
        });

        return converter;
    }

    @Bean
    public ApplicationListener<AbstractAuthenticationEvent> authenticationLogger() {
        return event -> {
            if (event instanceof AuthenticationSuccessEvent success) {
                log.atDebug()
                        .addArgument(success.getAuthentication().getName())
                        .log("Authentication success, login={}");
            }
            else if (event instanceof AbstractAuthenticationFailureEvent failure) {
                log.atDebug()
                        .addArgument(failure.getAuthentication().getName())
                        .log("Authentication failed, login={}");
            }
        };
    }

}
