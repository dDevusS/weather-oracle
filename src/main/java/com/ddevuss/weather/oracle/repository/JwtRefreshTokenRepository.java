package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.entity.JwtRefreshToken;
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

    @Modifying
    @Query(nativeQuery = true,
            value = "INSERT INTO jwt_refresh_tokens (user_id, token_hash, expires_at, created_at) " +
                    "VALUES ((SELECT id FROM users WHERE login = :#{#token.user.login}), " +
                    ":#{#token.tokenHash}, " +
                    ":#{#token.expiresAt}, " +
                    ":#{#token.createdAt})")
    void saveToken(JwtRefreshToken token);
}
