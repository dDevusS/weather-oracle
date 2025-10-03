package com.ddevuss.weather.oracle.auth.domain.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;

public interface JwtService {

    String generateAccessToken(String username, Instant createdAt);

    String exchangeRefreshToken(DecodedJWT decodedRefreshToChange, Instant createdAt);

    String generateAndSaveRefreshToken(String username, Instant createdAt);

    boolean isRefreshTokenType(DecodedJWT refreshToken);

    DecodedJWT verifyAndDecodeToken(String token) throws Exception;

    void revokeRefreshToken(DecodedJWT refreshToken);

}
