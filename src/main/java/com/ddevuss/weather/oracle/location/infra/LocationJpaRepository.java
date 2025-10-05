package com.ddevuss.weather.oracle.location.infra;

import com.ddevuss.weather.oracle.location.domain.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface LocationJpaRepository extends JpaRepository<Location, Long> {

    @Modifying
    int deleteAllByUserId(Long userId);

    Slice<Location> findAllByUserLogin(String login, Pageable pageable);

}