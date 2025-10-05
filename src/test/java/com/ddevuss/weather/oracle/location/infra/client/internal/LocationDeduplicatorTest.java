package com.ddevuss.weather.oracle.location.infra.client.internal;

import com.ddevuss.weather.oracle.location.domain.LocationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationDeduplicatorTest {

    private static LocationDto[] duplicatedLocations;
    private static LocationDto locationA;
    private static LocationDto locationB;
    private static LocationDto locationC;
    private static LocationDto locationD;

    @BeforeAll
    static void setUp() {
        locationA = LocationDto.builder()
                .name("CityA")
                .state("StateA")
                .country("CountryA")
                .lat(12.3456)
                .lon(78.9012)
                .build();

        locationB = LocationDto.builder()
                .name("CityB")
                .state("StateB")
                .country("CountryB")
                .lat(23.4567)
                .lon(89.0123)
                .build();

        locationC = LocationDto.builder()
                .name("CityC")
                .state(null)
                .country("CountryC")
                .lat(34.5678)
                .lon(90.1234)
                .build();

        locationD = LocationDto.builder()
                .name(null)
                .state(null)
                .country(null)
                .lat(45.6789)
                .lon(12.3456)
                .build();

        duplicatedLocations = new LocationDto[]{
                locationA,
                locationA, // Duplicate by name and truncated coords

                locationB,

                locationC,
                locationC, // Duplicate by name and truncated coords

                locationD,
                locationD // Duplicate by truncated coords
        };
    }

    @Test
    void deduplicate() {
        var deduplicated = LocationDeduplicator.deduplicate(duplicatedLocations);
        assertEquals(4, deduplicated.size());

        assertTrue(deduplicated.stream().anyMatch(l -> locationA.equals(l)));
        assertTrue(deduplicated.stream().anyMatch(l -> locationB.equals(l)));
        assertTrue(deduplicated.stream().anyMatch(l -> locationC.equals(l)));
        assertTrue(deduplicated.stream().anyMatch(l -> locationD.equals(l)));
    }
}