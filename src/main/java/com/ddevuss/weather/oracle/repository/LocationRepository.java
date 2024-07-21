package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Modifying
    int deleteAllByUserId(Long userId);

    @Query(value = "select l from Location l " +
                   "where l.user.id = :userId " +
                   "and l.latitude = :latitude " +
                   "and  l.longitude = :longitude")
    Optional<Location> findByUserIdAndLocationLatitudeAndLocationLongitude(Long userId, Double latitude, Double longitude);

    List<Location> findAllByUserLogin(String login);
}