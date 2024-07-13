package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.entity.Session;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public Session createSession(Integer userId) {
        User user = User.builder()
                .id(userId)
                .build();
        Timestamp expiresAt = Timestamp.valueOf(LocalDateTime.now().plusHours(3));
        return sessionRepository.save(new Session(null, user, expiresAt));
    }

    public UUID getSessionUuidByUserId(Integer userId) {
        Session session = sessionRepository.findByUserId(userId)
                .orElse(this.createSession(userId));
        return UUID.fromString(session.getId());
    }
}
