package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByUserId(Long userId);

    @Modifying
    int deleteAllByUserId(Long userId);

}
