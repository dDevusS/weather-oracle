package com.ddevuss.weather.oracle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record RefreshTokenDto(
        @JsonProperty("refresh_token")
        @NotBlank
        String refreshToken
) implements Serializable {}
