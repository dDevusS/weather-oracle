package com.ddevuss.weather.oracle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record RefreshTokenDto(
        @JsonProperty("refreshToken")
        @NotBlank
        String refreshToken
) implements Serializable {}
