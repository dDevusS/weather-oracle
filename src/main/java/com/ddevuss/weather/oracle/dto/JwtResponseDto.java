package com.ddevuss.weather.oracle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtResponseDto(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken
) {

}
