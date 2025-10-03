package com.ddevuss.weather.oracle.auth.domain.jwt.configuration.model;

import java.time.Duration;

public record TimeOfLife(Duration accessToken, Duration refreshToken) {

}
