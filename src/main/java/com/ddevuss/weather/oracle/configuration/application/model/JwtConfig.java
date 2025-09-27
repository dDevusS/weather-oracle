package com.ddevuss.weather.oracle.configuration.application.model;

public record JwtConfig(String secret, boolean isSecure, TimeOfLife timeOfLife) {

}
