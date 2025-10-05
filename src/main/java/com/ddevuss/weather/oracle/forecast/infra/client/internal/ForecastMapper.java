package com.ddevuss.weather.oracle.forecast.infra.client.internal;

import com.ddevuss.weather.oracle.forecast.domain.Forecast;
import com.ddevuss.weather.oracle.location.dto.LocationDto;

class ForecastMapper {

    public static Forecast fromResponse(OpenWeatherExternalDto response, LocationDto location) {
        return Forecast.builder()
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
