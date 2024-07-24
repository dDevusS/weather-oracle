package com.ddevuss.weather.oracle.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class UserReadDto implements Serializable {

    Long id;

    String login;
}
