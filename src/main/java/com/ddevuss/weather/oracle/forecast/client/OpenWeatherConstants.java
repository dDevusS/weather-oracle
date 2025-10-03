package com.ddevuss.weather.oracle.forecast.client;

public final class OpenWeatherConstants {

    public static final String DEFAULT_OPEN_WEATHER_API_URL = "https://api.openweathermap.org";

    public static final String PATH_GEO_DIRECT = "/geo/1.0/direct";
    public static final String PATH_WEATHER = "/data/2.5/weather";

    public static final String PARAM_APPID = "appid";
    public static final String PARAM_Q_FOR_CITY_NAME = "q";
    public static final String PARAM_LIMIT = "limit";
    public static final String PARAM_LAT = "lat";
    public static final String PARAM_LON = "lon";
    public static final String PARAM_UNITS = "units";

    public static final int DEFAULT_LIMIT = 5;
    public static final String UNITS_METRIC = "metric";
}
