package com.ddevuss.weather.oracle.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {

    Optional<JwtRefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    int deleteAllByExpiresAtBefore(Instant expiresAtBefore);

    @Modifying
    @Query("UPDATE JwtRefreshToken t " +
           "SET t.revoked = true " +
           "WHERE t.tokenHash = :tokenHash")
    int revokeByTokenHash(String tokenHash);

    @Modifying
    @Query("UPDATE JwtRefreshToken t " +
           "SET t.revoked = true " +
           "WHERE t.user.login = :login " +
           "AND t.revoked = false")
    int revokeByUserLogin(String login);

}
