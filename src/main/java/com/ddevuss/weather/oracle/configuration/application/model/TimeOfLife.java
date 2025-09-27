package com.ddevuss.weather.oracle.configuration.application.model;

import java.time.Duration;

public record TimeOfLife(Duration accessToken, Duration refreshToken) {

}
