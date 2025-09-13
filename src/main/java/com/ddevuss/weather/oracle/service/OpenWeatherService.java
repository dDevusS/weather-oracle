package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.configuration.WeatherOracleConfiguration;
import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationDto;
import com.ddevuss.weather.oracle.dto.externalApi.ForecastApiResponseDto;
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
import java.util.Optional;

@Slf4j
@Service
public class OpenWeatherService {

    private final RestClient openWeatherRestClient;
    private final String appId;
    private final static int TRUNCATE_VALUE = 2;
    private final static String KEY_FOR_APP_ID = "&appid=";

    private final static String GEO_API_FRAGMENT = "/geo/1.0/direct?";
    private final static String KEY_FOR_CITY_NAME = "q=";
    private final static String KEY_FOR_LIMITATION_RESPONSE = "&limit=";

    private final static int MAX_LIMITATION_RESPONSE = 5;

    private final static String WEATHER_API_FRAGMENT = "/data/2.5/weather?";
    private final static String KEY_FOR_LATITUDE = "lat=";
    private final static String KEY_FOR_LONGITUDE = "&lon=";
    private final static String UNITS_MEASUREMENT = "&units=metric";

    @Autowired
    public OpenWeatherService(RestClient openWeatherRestClient,
                              WeatherOracleConfiguration properties) {
        this.openWeatherRestClient = openWeatherRestClient;
        this.appId = properties.getOpenWeatherApi().key();
    }

    public List<LocationDto> searchLocationsByName(String locationName) {
        String url = buildUrlForGeoApi(locationName, appId);

        var locations = openWeatherRestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(LocationDto[].class);

        return removeDuplicates(locations);
    }

    public List<ForecastDto> getWeatherForecast(List<LocationDto> locations) {
        List<ForecastDto> forecasts = new ArrayList<>();

        for (LocationDto location : locations) {
            String url = buildUrlForWeatherApi(location.getLat(), location.getLon(), appId);

            var responseEntity = openWeatherRestClient.get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(s -> s.value() == 404, (req, res) -> {
                        log.info("OpenWeather: 404 for location id={} lat={}, lon={}",
                                location.getId(), location.getLat(), location.getLon());
                    })
                    .toEntity(ForecastApiResponseDto.class);

            if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
                continue;
            }
            forecasts.add(convertFromResponseDto(responseEntity.getBody(), location));
        }

        return forecasts;
    }

    public static String buildUrlForGeoApi(String locationName, String appId) {
        return GEO_API_FRAGMENT +
               KEY_FOR_CITY_NAME +
               locationName +
               KEY_FOR_LIMITATION_RESPONSE +
               MAX_LIMITATION_RESPONSE +
               KEY_FOR_APP_ID +
               appId;
    }

    public static String buildUrlForWeatherApi(Double latitude, Double longitude, String appId) {
        return WEATHER_API_FRAGMENT +
               KEY_FOR_LATITUDE +
               latitude +
               KEY_FOR_LONGITUDE +
               longitude +
               KEY_FOR_APP_ID +
               appId +
               UNITS_MEASUREMENT;
    }

    private static List<LocationDto> removeDuplicates(LocationDto[] locations) {
        return Arrays.stream(locations)
                .filter(Objects::nonNull)
                .filter(StreamUtils.distinctBy(l ->
                        Map.entry(MathUtil.truncateCoordinate(l.getLat(), TRUNCATE_VALUE),
                                MathUtil.truncateCoordinate(l.getLon(), TRUNCATE_VALUE)
                        )
                ))
                .filter(StreamUtils.distinctBy(l ->
                        List.of(
                                Optional.ofNullable(l.getName()).orElse(""),
                                Optional.ofNullable(l.getState()).orElse(""),
                                Optional.ofNullable(l.getCountry()).orElse("")
                        )
                ))
                .toList();
    }

    private static ForecastDto convertFromResponseDto(ForecastApiResponseDto response, LocationDto location) {
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
