package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.dto.LocationResponseDto;
import com.ddevuss.weather.oracle.dto.WeatherForecastDto;
import com.ddevuss.weather.oracle.dto.WeatherForecastResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenWeatherService {

    private final RestTemplate restTemplate;
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

    @Autowired
    public OpenWeatherService(RestTemplate restTemplate,
                              @Value("${openweather.api.key}") String appId) {
        this.restTemplate = restTemplate;
        this.appId = appId;
    }

    // http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&limit={limit}&appid={API key}

    public LocationResponseDto[] searchLocationByName(String locationName) {
        String url = new StringBuilder()
                .append(OPEN_WEATHER_URL)
                .append(GEO_API_FRAGMENT)
                .append(KEY_FOR_CITY_NAME)
                .append(locationName)
                .append(LIMIT_5_FOR_RESPONSE)
                .append(KEY_FOR_APP_ID)
                .append(appId)
                .toString();

        return restTemplate.getForObject(url, LocationResponseDto[].class);
    }

    // https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}

    public List<WeatherForecastDto> getWeatherForecast(List<LocationReadDto> locations) {
        List<WeatherForecastDto> forecasts = new ArrayList<>();

        for (LocationReadDto location : locations) {
            String url = new StringBuilder()
                    .append(OPEN_WEATHER_URL)
                    .append(WEATHER_API_FRAGMENT)
                    .append(KEY_FOR_LATITUDE)
                    .append(location.getLatitude())
                    .append(KEY_FOR_LONGITUDE)
                    .append(location.getLongitude())
                    .append(KEY_FOR_APP_ID)
                    .append(appId)
                    .append(UNITS_MEASUREMENT)
                    .toString();

            WeatherForecastResponseDto forecastResponse = restTemplate.getForObject(url, WeatherForecastResponseDto.class);
            forecasts.add(convertFromResponseDto(forecastResponse, location));
        }

        return forecasts;
    }

    private WeatherForecastDto convertFromResponseDto(WeatherForecastResponseDto response, LocationReadDto location) {
        return WeatherForecastDto.builder()
                .locationId(location.getId())
                .locationName(location.getName())
                .countryCode(response.getSys().getCountry())
                .description(response.getWeather()[0].getDescription())
                .temperature(response.getMain().getTemp())
                .feelsLikeTemperature(response.getMain().getFeelsLike())
                .pressure(response.getMain().getPressure())
                .humidity(response.getMain().getHumidity())
                .build();
    }

}
