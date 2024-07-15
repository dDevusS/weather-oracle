package com.ddevuss.weather.oracle.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class LocationReadDto {

    Integer id;

    String name;

    BigDecimal latitude;

    BigDecimal longitude;
}
