package com.ddevuss.weather.oracle.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserReadDto {

    Long id;

    String login;
}
