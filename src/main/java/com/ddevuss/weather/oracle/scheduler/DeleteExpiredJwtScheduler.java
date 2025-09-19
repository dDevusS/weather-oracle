package com.ddevuss.weather.oracle.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@AllArgsConstructor
@Component
public class DeleteExpiredJwtScheduler {

    private final com.ddevuss.weather.oracle.repository.JwtRefreshTokenRepository jwtRepository;

    @Scheduled(cron = "0 0 0/12 * * *")
    @Transactional
    public void deleteExpiredTokens() {
        jwtRepository.deleteAllByExpiresAtBefore(Instant.now());
    }
}
