package com.ddevuss.weather.oracle.controller.rest;

import com.ddevuss.weather.oracle.dto.LocationResponseDto;
import com.ddevuss.weather.oracle.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1")
public class OpenWeatherRestController {

    private final LocationService locationService;
    private final RestTemplate restTemplate;
    private final String appId;
    private final static String KEY_FOR_APP_ID = "&appid=";
    private final static String OPEN_WEATHER_URL = "https://api.openweathermap.org";
    private final static StringBuilder stringBuilder = new StringBuilder();

    // http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&limit={limit}&appid={API key}

    private final static String GEO_API_FRAGMENT = "/geo/1.0/direct?";
    private final static String KEY_FOR_QUESTION = "q=";
    private final static String LIMIT_5_FOR_RESPONSE = "&limit=5";

    // https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}

    private final static String WEATHER_API_FRAGMENT = "/data/2.5/weather?";
    private final static String KEY_FOR_LATITUDE = "lat=";
    private final static String KEY_FOR_LONGITUDE = "&lon=";

    @Autowired
    public OpenWeatherRestController(LocationService locationService,
                                     RestTemplate restTemplate,
                                     @Value("${openweather.api.key}") String appId) {
        this.locationService = locationService;
        this.restTemplate = restTemplate;
        this.appId = appId;
    }

    @GetMapping("/search")
    public LocationResponseDto[] searchLocation(@RequestParam String locationName) {
        String url = stringBuilder.append(OPEN_WEATHER_URL)
                .append(GEO_API_FRAGMENT)
                .append(KEY_FOR_QUESTION)
                .append(locationName)
                .append(LIMIT_5_FOR_RESPONSE)
                .append(KEY_FOR_APP_ID)
                .append(appId).toString();

        return restTemplate.getForObject(url, LocationResponseDto[].class);
    }
}