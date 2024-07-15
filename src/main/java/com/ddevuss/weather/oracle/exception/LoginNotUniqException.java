package com.ddevuss.weather.oracle.exception;

public class LoginNotUniqException extends RuntimeException {

    public LoginNotUniqException() {
        super("User with this login already exists");
    }
}
