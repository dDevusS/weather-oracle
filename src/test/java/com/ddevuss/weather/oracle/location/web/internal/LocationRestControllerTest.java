package com.ddevuss.weather.oracle.location.web.internal;

import com.ddevuss.weather.oracle.location.app.LocationService;
import com.ddevuss.weather.oracle.location.domain.LocationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("LocationRestController")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = LocationRestControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class LocationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LocationService locationService;

    private static final String DUMMY_STRING = "DUMMY";
    private static final LocationDto location = LocationDto.builder()
            .name(DUMMY_STRING)
            .country(DUMMY_STRING)
            .lat(0.0)
            .lon(0.0)
            .build();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("searchByName - returns list of locations when found")
    @Test
    void searchByName_returnsList() throws Exception {
        when(locationService.searchLocationByName(DUMMY_STRING))
                .thenReturn(List.of(LocationDto.builder().build(), LocationDto.builder().build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/locations/search")
                        .param("locationName", DUMMY_STRING)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> {
                            LocationDto[] locations = objectMapper.readValue(result.getResponse().getContentAsString(), LocationDto[].class);
                            assertThat(locations.length).isEqualTo(2);
                        }
                );

        verify(locationService).searchLocationByName(DUMMY_STRING);
    }

    @SneakyThrows
    @DisplayName("searchByName - returns 400 when locationName is missing or empty")
    @NullAndEmptySource
    @ParameterizedTest
    void searchByName_returnsBadRequest(String locationName) {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/locations/search")
                        .param("locationName", locationName)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @DisplayName("save - returns 201 and saved location")
    @Test
    void save() {
        when(locationService.save(location, DUMMY_STRING))
                .thenReturn(location);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/locations")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(location))
                        .accept(APPLICATION_JSON)
                        .principal((Principal) () -> DUMMY_STRING)
                ).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> {
                    LocationDto returnedLocation = objectMapper.readValue(result.getResponse().getContentAsString(), LocationDto.class);
                    assertThat(returnedLocation).isEqualTo(location);
                });

        verify(locationService).save(location, DUMMY_STRING);
    }

    @DisplayName("delete - returns 204 when deletion is successful or returns 400 when locationId is negative")
    @MethodSource(value = "com.ddevuss.weather.oracle.location.web.internal.LocationRestControllerTest#provideArgumentsForDeleteTest")
    @ParameterizedTest
    void delete(Long locationId, int expectedStatus) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/locations/{id}", locationId))
                .andExpect(status().is(expectedStatus));

        if (expectedStatus == 204) {
            verify(locationService).deleteById(locationId);
        }
    }

    static Stream<Arguments> provideArgumentsForDeleteTest() {
        return Stream.of(
                Arguments.of(1L, 204),
                Arguments.of(-1L, 400)
        );
    }

    @Test
    void get() {
    }
}