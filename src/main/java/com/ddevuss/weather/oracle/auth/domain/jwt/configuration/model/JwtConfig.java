package com.ddevuss.weather.oracle.auth.domain.jwt.configuration.model;

public record JwtConfig(String secret, boolean isSecure, TimeOfLife timeOfLife) {

}
