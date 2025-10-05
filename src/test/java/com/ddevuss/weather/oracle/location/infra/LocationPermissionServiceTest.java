package com.ddevuss.weather.oracle.location.infra;

import jakarta.persistence.EntityNotFoundException;
import com.ddevuss.weather.oracle.auth.domain.user.User;
import com.ddevuss.weather.oracle.location.domain.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import org.springframework.security.access.AccessDeniedException;
import java.util.Optional;

import static com.ddevuss.weather.oracle.common.Constants.CORRELATION_ID;
import static com.ddevuss.weather.oracle.common.Constants.USERNAME;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocationPermissionServiceTest {

    @Mock
    private LocationJpaRepository locationJpaRepository;
    @InjectMocks
    private LocationPermissionService locationPermissionService;

    private static final Long LOCATION_ID_1 = 1L;
    private static final Long LOCATION_ID_2 = 2L;
    private static final Long LOCATION_ID_DUMMY = 100L;
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_ANOTHER_USERNAME = "anotherUser";
    private static final String TEST_CORRELATION_ID = "testCorrelationId";

    @BeforeEach
    void setUp() {
        MDC.put(USERNAME, TEST_USERNAME);
        MDC.put(CORRELATION_ID, TEST_CORRELATION_ID);

        when(locationJpaRepository.findById(LOCATION_ID_1))
                .thenReturn(Optional.of(Location.builder()
                                .id(LOCATION_ID_1)
                                .user(User.builder()
                                        .login(TEST_USERNAME)
                                        .build())
                                .build()
                        )
                );

        when(locationJpaRepository.findById(LOCATION_ID_2))
                .thenReturn(Optional.of(Location.builder()
                                .id(LOCATION_ID_2)
                                .user(User.builder()
                                        .login(TEST_ANOTHER_USERNAME)
                                        .build())
                                .build()
                        )
                );
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @DisplayName("permission to delete location")
    @Test
    void hasPermissionToDeleteLocation() {
        assertTrue(locationPermissionService.hasPermissionToDeleteLocation(LOCATION_ID_1),
                "Expected: User should have permission to delete their own location");

        assertThrows(AccessDeniedException.class, () -> locationPermissionService.hasPermissionToDeleteLocation(LOCATION_ID_2),
                "Expected: User should not have permission to delete another user's location");

        assertThrows(EntityNotFoundException.class, () -> locationPermissionService.hasPermissionToDeleteLocation(LOCATION_ID_DUMMY),
                "Expected: EntityNotFoundException should be thrown for non-existent location");
    }

}