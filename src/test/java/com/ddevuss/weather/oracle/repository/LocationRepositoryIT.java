package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.IntegrationTestBase;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class LocationRepositoryIT extends IntegrationTestBase {

    private final LocationRepository locationRepository;

    @Test
    void findByUserId() {
        var locations = locationRepository.findByUserId(1);

        assertThat(locations).hasSize(2);
    }

    @Test
    void deleteAllByUserId() {
        var result = locationRepository.deleteAllByUserId(1);

        assertThat(result).isEqualTo(2);
    }

    @Test
    void deleteById() {
        locationRepository.deleteById(1);

        assertThat(locationRepository.findById(1)).isEmpty();
    }

    @Test
    void save() {
        Location newLocation = Location.builder()
                .name("Location")
                .userId(User.builder()
                        .id(1)
                        .build())
                .latitude(BigDecimal.valueOf(40.2345))
                .longitude(BigDecimal.valueOf(45.2345))
                .build();

        newLocation = locationRepository.save(newLocation);
        assertThat(newLocation.getId()).isNotNull();
    }

}