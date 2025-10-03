package com.ddevuss.weather.oracle.auth.domain.jwt.scheduler;

import com.ddevuss.weather.oracle.auth.domain.jwt.JwtRefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@AllArgsConstructor
@Component
public class DeleteExpiredJwtScheduler {

    private final JwtRefreshTokenRepository jwtRepository;

    @Scheduled(cron = "${application.jwt.cleanup.schedule:0 0 0/12 * * *}", zone = "${application.jwt.cleanup.zone:UTC}")
    @Transactional
    public void deleteExpiredTokens() {
        jwtRepository.deleteAllByExpiresAtBefore(Instant.now());
    }
}
