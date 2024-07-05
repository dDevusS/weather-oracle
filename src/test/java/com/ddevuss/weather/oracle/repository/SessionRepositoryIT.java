package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.IntegrationTestBase;
import com.ddevuss.weather.oracle.entity.Session;
import com.ddevuss.weather.oracle.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class SessionRepositoryIT extends IntegrationTestBase {

    private final SessionRepository sessionRepository;

    @Test
    void findSession() {
        UUID uuid = UUID.fromString("e819b5f1-23c4-4a26-a1c0-0b37b21896f8");

        var session = sessionRepository.findById(uuid.toString());
        assertThat(session.isPresent()).isTrue();
    }

    @Test
    void saveSession() {
        Session newSession = Session.builder()
                .user(User.builder()
                        .id(7)
                        .build())
                .expiresAt(Timestamp.valueOf(LocalDateTime.now().minusMonths(1)))
                .build();

        newSession = sessionRepository.save(newSession);
        assertThat(newSession.getId()).isNotNull();
    }

    @Test
    void deleteSession() {
        UUID uuid = UUID.fromString("e819b5f1-23c4-4a26-a1c0-0b37b21896f8");

        sessionRepository.deleteById(uuid.toString());
        assertThat(sessionRepository.findById(uuid.toString()).isPresent()).isFalse();
    }

    @Test
    void updateExpiresTime() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().plusMonths(1));

        var result = sessionRepository.updateExpiresTimeByUserId(1, timestamp);
        assertThat(result).isEqualTo(1);
        assertThat(sessionRepository.findByUserId(1).get().getExpiresAt().getTime())
                .isEqualTo(timestamp.getTime());
    }

    @Test
    void findByUserId() {
        var session = sessionRepository.findByUserId(1);

        assertThat(session.isPresent()).isTrue();
    }
}