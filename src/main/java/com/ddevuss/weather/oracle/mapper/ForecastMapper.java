package com.ddevuss.weather.oracle.mapper;

import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationDto;
import com.ddevuss.weather.oracle.dto.externalApi.ForecastApiResponseDto;

public class ForecastMapper {

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
