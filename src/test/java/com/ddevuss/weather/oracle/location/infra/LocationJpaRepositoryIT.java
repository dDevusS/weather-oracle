package com.ddevuss.weather.oracle.location.infra;

import com.ddevuss.weather.oracle.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class LocationJpaRepositoryIT extends IntegrationTestBase {

    private final LocationJpaRepository locationJpaRepository;
    private final static String USER_LOGIN = "user1";

    @Test
    void findAllByUserLogin() {
        int numberElementsOnPage = 4;
        PageRequest pageRequest = PageRequest.of(0, numberElementsOnPage);
        var locations = locationJpaRepository.findAllByUserLogin(USER_LOGIN, pageRequest);

        assertThat(locations.getNumberOfElements()).isEqualTo(2);
    }

}