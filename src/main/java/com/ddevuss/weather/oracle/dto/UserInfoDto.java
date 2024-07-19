package com.ddevuss.weather.oracle.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@Builder
public class UserInfoDto implements Serializable {

    UserReadDto user;

    List<ForecastDto> forecasts;

}
