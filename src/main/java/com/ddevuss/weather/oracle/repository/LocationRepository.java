package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.entity.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Modifying
    int deleteAllByUserId(Long userId);

    Slice<Location> findAllByUserLogin(String login, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "INSERT INTO locations (name, state, user_id, latitude, longitude) " +
                    "VALUES (:#{#location.name}, " +
                    ":#{#location.state}, " +
                    "(SELECT id FROM users WHERE login = :#{#location.user.login}), " +
                    ":#{#location.latitude}, " +
                    ":#{#location.longitude})")
    void saveLocation(@Param("location") Location location);
}