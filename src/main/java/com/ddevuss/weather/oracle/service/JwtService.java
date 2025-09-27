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
import com.ddevuss.weather.oracle.security.jwt.TokenType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

import static com.ddevuss.weather.oracle.security.jwt.JwtClaims.TYPE;

@Slf4j
@Service
public class JwtService {

    private final JwtRefreshTokenRepository jwtRepository;
    private final String secret;
    private final Duration accessExpiration;
    private final Duration refreshExpiration;

    @Autowired
    public JwtService(WeatherOracleConfiguration configuration,
                      JwtRefreshTokenRepository jwtRefreshTokenRepository) {
        this.secret = configuration.getJwt().secret();
        this.accessExpiration = configuration.getJwt().timeOfLife().accessToken();
        this.refreshExpiration = configuration.getJwt().timeOfLife().refreshToken();
        this.jwtRepository = jwtRefreshTokenRepository;
    }

    public String generateAccessToken(String username, Instant createdAt) {
        return generateJwtToken(username, createdAt, accessExpiration, TypeOfToken.ACCESS_TOKEN);
    }

    @SneakyThrows
    @Transactional
    public String exchangeRefreshToken(DecodedJWT decodedRefreshToChange, Instant createdAt) {
        revokeRefreshToken(decodedRefreshToChange);

        return generateAndSaveRefreshToken(decodedRefreshToChange.getSubject(), createdAt);
    }

    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRED)
    public String generateAndSaveRefreshToken(String username, Instant createdAt) {
        String newRefreshToken = generateJwtToken(username, createdAt, jwtConfig.timeOfLife().refreshToken(), TokenType.REFRESH_TOKEN);
        User user = userRepository.findByLogin(username).orElseThrow();

        JwtRefreshToken token = JwtRefreshToken.builder()
                .user(User.builder().login(username).build())
                .createdAt(createdAt)
                .expiresAt(createdAt.plus(refreshExpiration))
                .tokenHash(generateTokenHash(newRefreshToken))
                .build();

        jwtRepository.saveToken(token);

        return newRefreshToken;
    }

    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRED)
    public void revokeRefreshToken(DecodedJWT decodedRefreshToken) {
        String refreshTokenHash = (generateTokenHash(decodedRefreshToken.getToken()));
        jwtRepository.revokeByTokenHash(refreshTokenHash);
    }

    public boolean isRefreshTokenType(DecodedJWT refreshToken) {
        return TokenType.REFRESH_TOKEN.getCode().equals(refreshToken.getClaim(TYPE).asString());
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

    @SneakyThrows
    private boolean isRefreshTokenExistsAndNotRevoked(DecodedJWT refreshToken) {
        String tokenHash = generateTokenHash(refreshToken.getToken());
        return jwtRepository.findByTokenHash(tokenHash)
                .map(token -> !token.isRevoked())
                .orElse(false);
    }

    private String generateTokenHash(String token) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        mac.init(keySpec);

        byte[] hash = mac.doFinal(token.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    private String generateJwtToken(String username, Instant createdAt, Duration expirationTime, TokenType tokenType) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.secret());
        Instant expiredAt = createdAt.plus(expirationTime);
        Map<String, Object> payload = new HashMap<>();
        payload.put(TYPE, tokenType.getCode());

        return JWT.create()
                .withSubject(username)
                .withPayload(payload)
                .withIssuedAt(createdAt)
                .withExpiresAt(expiredAt)
                .sign(algorithm);
    }
}
