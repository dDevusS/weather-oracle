package com.ddevuss.weather.oracle.forecast.api.internal;

import com.ddevuss.weather.oracle.forecast.dto.ForecastDto;
import com.ddevuss.weather.oracle.location.LocationDto;

class ForecastMapper {

    public static ForecastDto fromResponse(ForecastApiResponseDto response, LocationDto location) {
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
