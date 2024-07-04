package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query(value = "select l " +
                   "from Location l " +
                   "where l.userId.id = :userId")
    List<Location> findByUserId(Integer userId);

    @Modifying
    @Query(value = "delete from Location l " +
                   "where l.userId.id = :userId")
    int deleteAllByUserId(Integer userId);

}
