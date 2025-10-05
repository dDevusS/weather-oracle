package com.ddevuss.weather.oracle.location.web.internal;

import com.ddevuss.weather.oracle.location.app.LocationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("LocationRestController")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = LocationRestControllerImpl.class)
class LocationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    private static final String LOCATION_NAME = "SomeLocation";


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("searchByName")
    @Test
    void searchByName() {

    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    @Test
    void get() {
    }
}