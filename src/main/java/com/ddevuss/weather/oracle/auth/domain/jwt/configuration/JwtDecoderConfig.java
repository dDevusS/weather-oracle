package com.ddevuss.weather.oracle.auth.domain.jwt.configuration;

import com.ddevuss.weather.oracle.auth.domain.jwt.TokenType;
import com.ddevuss.weather.oracle.auth.domain.jwt.configuration.model.JwtConfig;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static com.ddevuss.weather.oracle.auth.domain.jwt.JwtConstants.*;
import static com.ddevuss.weather.oracle.common.Constants.ALGORITHM;
import static com.ddevuss.weather.oracle.common.Constants.USERNAME;

@Configuration
public class JwtDecoderConfig {

    private static final String JWT_INVALID_TYPE_MESSAGE = "Invalid token type: ";
    private static final String CLAIM_FIELD_SUB = "sub";

    @Bean
    public JwtDecoder jwtDecoder(JwtConfig properties) {
        SecretKey key = new SecretKeySpec(properties.secret().getBytes(), ALGORITHM);
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String type = jwt.getClaimAsString(TYPE);

            if (!TokenType.ACCESS_TOKEN.getCode().equals(type)) {
                throw new JwtException(JWT_INVALID_TYPE_MESSAGE + type);
            }

            MDC.put(USERNAME, jwt.getClaimAsString(CLAIM_FIELD_SUB));

            return AuthorityUtils.NO_AUTHORITIES;
        });

        return converter;
    }
}
