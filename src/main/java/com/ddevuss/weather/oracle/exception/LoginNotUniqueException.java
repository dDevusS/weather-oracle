package com.ddevuss.weather.oracle.exception;

public class LoginNotUniqueException extends RuntimeException {

    public LoginNotUniqueException() {
        super("User with this login already exists");
    }
}
