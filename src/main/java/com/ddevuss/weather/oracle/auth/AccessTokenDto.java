package com.ddevuss.weather.oracle.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record AccessTokenDto(
        @JsonProperty("accessToken")
        String accessToken
) implements Serializable {}
