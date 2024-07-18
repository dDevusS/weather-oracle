package com.ddevuss.weather.oracle.dto;

import com.ddevuss.weather.oracle.dto.api.WeatherForecastDto;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@Builder
public class UserInfoDto implements Serializable {

    Long id;

    String login;

    List<WeatherForecastDto> forecasts;

}
