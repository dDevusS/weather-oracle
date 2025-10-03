package com.ddevuss.weather.oracle.auth.domain.jwt.configuration;

import com.ddevuss.weather.oracle.auth.domain.jwt.configuration.model.JwtConfig;
import com.ddevuss.weather.oracle.auth.domain.jwt.configuration.model.TimeOfLife;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ddevuss.weather.oracle.auth.domain.jwt.JwtConstants.BYTES_FOR_JWT_SECRET;

@Slf4j
@Configuration
public class JwtConfiguration {

    private static final String NEW_SECRET_GENERATION_MESSAGE = "JWT secret is not set or invalid, generating new one";

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
        log.warn(NEW_SECRET_GENERATION_MESSAGE);

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
