package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {

    @Modifying
    @Query(value = "update Session s " +
                   "set s.expiresAt = :expiresAt " +
                   "where s.user.id = :userId")
    int updateExpiresTimeByUserId(Integer userId, Timestamp expiresAt);

    Optional<Session> findByUserId(Integer userId);

}
