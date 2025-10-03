package com.ddevuss.weather.oracle.auth.domain.jwt;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS_TOKEN("access"),
    REFRESH_TOKEN("refresh");

    private final String code;

    TokenType(String code) {
        this.code = code;
    }
}
