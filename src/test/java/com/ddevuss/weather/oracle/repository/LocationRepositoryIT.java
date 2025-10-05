package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.IntegrationTestBase;
import com.ddevuss.weather.oracle.location.domain.Location;
import com.ddevuss.weather.oracle.auth.domain.user.User;
import com.ddevuss.weather.oracle.location.infra.LocationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class LocationRepositoryIT extends IntegrationTestBase {

    private final LocationJpaRepository locationJpaRepository;
    private final static String USER_LOGIN = "user1";

    @Test
    void findAllByUserLogin() {
        int numberElementsOnPage = 4;
        PageRequest pageRequest = PageRequest.of(0, numberElementsOnPage);
        var locations = locationJpaRepository.findAllByUserLogin(USER_LOGIN, pageRequest);

        assertThat(locations.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    void deleteAllByUserId() {
        var result = locationJpaRepository.deleteAllByUserId(1L);

        assertThat(result).isEqualTo(2);
    }

    @Test
    void deleteById() {
        locationJpaRepository.deleteById(1L);

        assertThat(locationJpaRepository.findById(1L)).isEmpty();
    }

    @Test
    void save() {
        Location newLocation = Location.builder()
                .name("Location")
                .state("state")
                .user(User.builder()
                        .id(1L)
                        .build())
                .latitude(40.2345)
                .longitude(45.2345)
                .build();

        newLocation = locationJpaRepository.save(newLocation);
        assertThat(newLocation.getId()).isNotNull();
    }

}