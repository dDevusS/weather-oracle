package com.ddevuss.weather.oracle.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserCreateDto {

    String login;

    String rawPassword;
}
