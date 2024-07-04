package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, String> {}
