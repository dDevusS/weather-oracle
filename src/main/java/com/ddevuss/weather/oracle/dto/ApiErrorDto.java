package com.ddevuss.weather.oracle.dto;

public record ApiErrorDto(
        String field,
        String message,
        String errorCode
) {

}
