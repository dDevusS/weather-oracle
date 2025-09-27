package com.ddevuss.weather.oracle.security.jwt;

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
