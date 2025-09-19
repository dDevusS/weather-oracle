package com.ddevuss.weather.oracle.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ddevuss.weather.oracle.configuration.WeatherOracleConfiguration;
import com.ddevuss.weather.oracle.entity.JwtRefreshToken;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.repository.JwtRefreshTokenRepository;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
//@Transactional(readOnly = true)
public class JwtService {

    private final JwtRefreshTokenRepository jwtRepository;
    private final String secret;
    private static final Duration ACCESS_EXPIRATION = Duration.ofMinutes(5);
    private static final Duration REFRESH_EXPIRATION = Duration.ofDays(3);

    @Autowired
    public JwtService(WeatherOracleConfiguration configuration,
                      JwtRefreshTokenRepository jwtRefreshTokenRepository) {
        this.secret = configuration.getJwt().secret();
        this.jwtRepository = jwtRefreshTokenRepository;
    }

    public String generateAccessToken(String username, Instant createdAt) {
        return generateJwtToken(username, createdAt, ACCESS_EXPIRATION, TypeOfToken.ACCESS_TOKEN);
    }

    @Transactional
    @SneakyThrows
    public String exchangeRefreshToken(DecodedJWT decodedRefreshToChange, Instant createdAt) {
        revokeRefreshToken(decodedRefreshToChange);

        String newRefreshToken = generateRefreshToken(decodedRefreshToChange.getSubject(), createdAt);
        String tokenHash = generateTokenHash(newRefreshToken);

        JwtRefreshToken token = JwtRefreshToken.builder()
                .user(User.builder().login(decodedRefreshToChange.getSubject()).build())
                .createdAt(createdAt)
                .expiresAt(createdAt.plus(REFRESH_EXPIRATION))
                .tokenHash(tokenHash)
                .build();

        jwtRepository.saveToken(token);

        return newRefreshToken;

    }

    @SneakyThrows
    @Transactional
    public void saveRefreshToken(String username, Instant createdAt, String codedToken) {
        JwtRefreshToken token = JwtRefreshToken.builder()
                .user(User.builder().login(username).build())
                .createdAt(createdAt)
                .expiresAt(createdAt.plus(REFRESH_EXPIRATION))
                .tokenHash(generateTokenHash(codedToken))
                .build();

        jwtRepository.saveToken(token);
    }

    public String generateRefreshToken(String username, Instant createdAt) {
        return generateJwtToken(username, createdAt, REFRESH_EXPIRATION, TypeOfToken.REFRESH_TOKEN);
    }

    @SneakyThrows
    public boolean isRefreshTokenExistsAndNotRevoked(DecodedJWT refreshToken) {
        String tokenHash = generateTokenHash(refreshToken.getToken());
        return jwtRepository.findByTokenHash(tokenHash)
                .map(token -> !token.isRevoked())
                .orElse(false);
    }

    public boolean isRefreshTokenType(DecodedJWT refreshToken) {
        return "refresh".equals(refreshToken.getClaim("type").asString());
    }

    @SneakyThrows
    @Transactional(noRollbackFor = JWTVerificationException.class)
    public DecodedJWT verifyAndDecodeToken(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);

        if (!isRefreshTokenType(decodedJWT) || !isRefreshTokenExistsAndNotRevoked(decodedJWT)) {
            throw new JWTVerificationException("Invalid refresh token");
        }

        return decodedJWT;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeAllUserTokens(String username) {
        //Self-invocation move to another service?
        jwtRepository.revokeByUserLogin(username);
    }

    private String generateTokenHash(String token) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);

        byte[] hash = mac.doFinal(token.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    private String generateJwtToken(String username, Instant createdAt, Duration expirationTime, TypeOfToken tokenType) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Instant expiredAt = createdAt.plus(expirationTime);
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", tokenType.getType());

        return JWT.create()
                .withSubject(username)
                .withPayload(payload)
                .withIssuedAt(createdAt)
                .withExpiresAt(expiredAt)
                .sign(algorithm);
    }

    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRED)
    public void revokeRefreshToken(DecodedJWT decodedRefreshToken) {
        String refreshTokenHash = (generateTokenHash(decodedRefreshToken.getToken()));
        jwtRepository.revokeByTokenHash(refreshTokenHash);
    }

    @Getter
    private enum TypeOfToken {
        ACCESS_TOKEN("access"),
        REFRESH_TOKEN("refresh");

        private final String type;

        TypeOfToken(String type) {
            this.type = type;
        }
    }
}
