package com.ddevuss.weather.oracle.forecast;

import org.springframework.web.util.UriComponentsBuilder;

public final class OpenWeatherUrlBuilder {

    public static String buildUrlForGeoApi(String locationName, String appId) {
        return UriComponentsBuilder.fromPath(PATH_GEO_DIRECT)
                .queryParam(PARAM_Q_FOR_CITY_NAME, locationName)
                .queryParam(PARAM_LIMIT, DEFAULT_LIMIT)
                .queryParam(PARAM_APPID, appId)
                .build()
                .toString();
    }

    public static String buildUrlForWeatherApi(Double latitude, Double longitude, String appId) {
        return UriComponentsBuilder.fromPath(PATH_WEATHER)
                .queryParam(PARAM_LAT, latitude)
                .queryParam(PARAM_LON, longitude)
                .queryParam(PARAM_APPID, appId)
                .queryParam(PARAM_UNITS, UNITS_METRIC)
                .build()
                .toString();
    }
}
