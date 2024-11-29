package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.dto.api.ForecastApiResponseDto;
import com.ddevuss.weather.oracle.dto.api.LocationApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OpenWeatherService {

    private final RestClient restClient;
    private final String appId;
    private final static String KEY_FOR_APP_ID = "&appid=";
    private final static String OPEN_WEATHER_URL = "https://api.openweathermap.org";

    private final static String GEO_API_FRAGMENT = "/geo/1.0/direct?";
    private final static String KEY_FOR_CITY_NAME = "q=";
    private final static String LIMIT_5_FOR_RESPONSE = "&limit=5";

    private final static String WEATHER_API_FRAGMENT = "/data/2.5/weather?";
    private final static String KEY_FOR_LATITUDE = "lat=";
    private final static String KEY_FOR_LONGITUDE = "&lon=";
    private final static String UNITS_MEASUREMENT = "&units=metric";

    private final static String ICON_URL_PATH = "/img/weather_ico/";
    private final static String ICON_SUFFIX = ".png";

    @Autowired
    public OpenWeatherService(RestClient restClient,
                              @Value("${openweather.api.key}") String appId) {
        this.restClient = restClient;
        this.appId = appId;
    }

    public LocationApiResponseDto[] searchLocationsByName(String locationName) {
        String url = buildUrlForGeoApi(locationName);

        return restClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(LocationApiResponseDto[].class);
    }

    public List<ForecastDto> getWeatherForecast(List<LocationReadDto> locations) {
        List<ForecastDto> forecasts = new ArrayList<>();

        for (LocationReadDto location : locations) {
            String url = buildUrlForWeatherApi(location.getLatitude(), location.getLongitude());

            ForecastApiResponseDto forecastResponse = restClient.get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(ForecastApiResponseDto.class);

            forecasts.add(convertFromResponseDto(forecastResponse, location));
        }

        return forecasts;
    }

    private String buildUrlForGeoApi(String locationName) {
        return OPEN_WEATHER_URL +
               GEO_API_FRAGMENT +
               KEY_FOR_CITY_NAME +
               locationName +
               LIMIT_5_FOR_RESPONSE +
               KEY_FOR_APP_ID +
               appId;
    }

    private String buildUrlForWeatherApi(Double latitude, Double longitude) {
        return OPEN_WEATHER_URL +
               WEATHER_API_FRAGMENT +
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
                .iconUrl(createIconUrl(response))
                .description(response.getWeather()[0].getDescription())
                .temperature(response.getMain().getTemp())
                .pressure(response.getMain().getPressure())
                .humidity(response.getMain().getHumidity())
                .build();
    }

    private String createIconUrl(ForecastApiResponseDto response) {
        return ICON_URL_PATH + response.getWeather()[0].getIcon() + ICON_SUFFIX;
    }

}
