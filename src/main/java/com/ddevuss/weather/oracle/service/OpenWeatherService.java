package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.configuration.WeatherOracleConfiguration;
import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.dto.api.ForecastApiResponseDto;
import com.ddevuss.weather.oracle.dto.api.LocationApiResponseDto;
import com.ddevuss.weather.oracle.utils.MathUtil;
import com.ddevuss.weather.oracle.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class OpenWeatherService {

    private final RestClient openWeatherRestClient;
    private final String appId;
    private final static String KEY_FOR_APP_ID = "&appid=";

    private final static String GEO_API_FRAGMENT = "/geo/1.0/direct?";
    private final static String KEY_FOR_CITY_NAME = "q=";
    private final static String LIMIT_5_FOR_RESPONSE = "&limit=5";

    private final static String WEATHER_API_FRAGMENT = "/data/2.5/weather?";
    private final static String KEY_FOR_LATITUDE = "lat=";
    private final static String KEY_FOR_LONGITUDE = "&lon=";
    private final static String UNITS_MEASUREMENT = "&units=metric";

    @Autowired
    public OpenWeatherService(RestClient openWeatherRestClient,
                              WeatherOracleConfiguration properties) {
        this.openWeatherRestClient = openWeatherRestClient;
        this.appId = properties.getKey();
    }

    public List<LocationApiResponseDto> searchLocationsByName(String locationName) {
        String url = buildUrlForGeoApi(locationName);

        var locations = openWeatherRestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(LocationApiResponseDto[].class);

        return Arrays.stream(locations)
                .filter(Objects::nonNull)
                .filter(StreamUtils.distinctBy(l ->
                        Map.entry(MathUtil.truncateCoordinate(l.getLat(), 2), MathUtil.truncateCoordinate(l.getLon(), 2))))
                .toList();
    }

    public List<ForecastDto> getWeatherForecast(List<LocationReadDto> locations) {
        List<ForecastDto> forecasts = new ArrayList<>();

        for (LocationReadDto location : locations) {
            String url = buildUrlForWeatherApi(location.getLatitude(), location.getLongitude());

            ForecastApiResponseDto forecastResponse = openWeatherRestClient.get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(ForecastApiResponseDto.class);

            forecasts.add(convertFromResponseDto(forecastResponse, location));
        }

        return forecasts;
    }

    private String buildUrlForGeoApi(String locationName) {
        return GEO_API_FRAGMENT +
               KEY_FOR_CITY_NAME +
               locationName +
               LIMIT_5_FOR_RESPONSE +
               KEY_FOR_APP_ID +
               appId;
    }

    private String buildUrlForWeatherApi(Double latitude, Double longitude) {
        return WEATHER_API_FRAGMENT +
               KEY_FOR_LATITUDE +
               latitude +
               KEY_FOR_LONGITUDE +
               longitude +
               KEY_FOR_APP_ID +
               appId +
               UNITS_MEASUREMENT;
    }

    private ForecastDto convertFromResponseDto(ForecastApiResponseDto response, LocationReadDto location) {
        return ForecastDto.builder()
                .locationId(location.getId())
                .locationName(location.getName())
                .countryCode(response.getSys().getCountry())
                .state(location.getState())
                .iconUrl(response.getWeather()[0].getIcon())
                .description(response.getWeather()[0].getDescription())
                .temperature(response.getMain().getTemp())
                .pressure(response.getMain().getPressure())
                .humidity(response.getMain().getHumidity())
                .build();
    }

}
