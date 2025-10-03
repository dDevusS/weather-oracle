package com.ddevuss.weather.oracle.auth.web.internal;

import com.ddevuss.weather.oracle.configuration.application.model.JwtConfig;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@AllArgsConstructor
@Service
class CookieCreatorService {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private final JwtConfig jwtConfig;

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return createHttpOnlyCookie(refreshToken, jwtConfig.timeOfLife().refreshToken());
    }

    public ResponseCookie deleteRefreshTokenCookie() {
        return createHttpOnlyCookie(null, Duration.ZERO);
    }

    private ResponseCookie createHttpOnlyCookie(@Nullable String cookieValue, Duration maxAge) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, cookieValue)
                .httpOnly(true)
                .secure(jwtConfig.isSecure())
                .path("/api/auth")
                .sameSite("Lax")
                .maxAge(maxAge)
                .build();
    }
}
