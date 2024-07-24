package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.dto.api.ForecastApiResponseDto;
import com.ddevuss.weather.oracle.dto.api.LocationApiResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenWeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenWeatherService openWeatherService;

    @Test
    void searchLocationsByName() {
        LocationApiResponseDto[] mockResponse = new LocationApiResponseDto[]{
                LocationApiResponseDto.builder()
                        .name("London")
                        .country("GB")
                        .state("GB")
                        .lat(50.0)
                        .lon(50.0)
                        .build()
        };

        when(restTemplate.getForObject(anyString(),
                eq(LocationApiResponseDto[].class)))
                .thenReturn(mockResponse);
        LocationApiResponseDto[] result = openWeatherService.searchLocationsByName("London");

        assertAll(() -> {
            assertNotNull(result, "Result should not be null");
            assertThat(result.length).isEqualTo(1);
            assertThat(result[0].getName()).isEqualTo("London");
            assertThat(result[0].getCountry()).isEqualTo("GB");
            assertThat(result[0].getState()).isEqualTo("GB");
            assertThat(result[0].getLat()).isEqualTo(50.0);
            assertThat(result[0].getLon()).isEqualTo(50.0);
            verify(restTemplate, times(1)
                    .description("restTemplate should be used 1 time"))
                    .getForObject(anyString(), eq(LocationApiResponseDto[].class));
        });
    }

    @Test
    void getWeatherForecast() {
        var weatherMockObject = new ForecastApiResponseDto.WeatherDescription();
        weatherMockObject.setIcon("dummy");
        weatherMockObject.setDescription("dummy");

        var sysInfoMockObject = new ForecastApiResponseDto.SysInfo();
        sysInfoMockObject.setCountry("dummy");

        var mainInfoMockObject = new ForecastApiResponseDto.MainInfo();
        mainInfoMockObject.setTemp(1f);
        mainInfoMockObject.setHumidity(1);
        mainInfoMockObject.setPressure(1);

        var mockResponse = ForecastApiResponseDto.builder()
                .weather(new ForecastApiResponseDto.WeatherDescription[]{
                        weatherMockObject
                })
                .sys(sysInfoMockObject)
                .main(mainInfoMockObject)
                .build();

        List<LocationReadDto> locations = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            locations.add(
                    LocationReadDto.builder()
                            .name("dummy")
                            .id(1L)
                            .latitude(1.0)
                            .longitude(1.0)
                            .build()
            );
        }

        when(restTemplate.getForObject(anyString(),
                eq(ForecastApiResponseDto.class)))
                .thenReturn(mockResponse);
        List<ForecastDto> result = openWeatherService.getWeatherForecast(locations);

        assertAll(() -> {
            assertThat(result.isEmpty()).isFalse().describedAs("Result should not be empty");
            assertThat(result.size()).isEqualTo(3).describedAs("Result size should be 3");
            verify(restTemplate, times(3)
                    .description("restTemplate should be used 3 times"))
                    .getForObject(anyString(), eq(ForecastApiResponseDto.class));
        });

    }
}