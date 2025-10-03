package com.ddevuss.weather.oracle.auth.validation.implementation;

import com.ddevuss.weather.oracle.auth.UserDto;
import com.ddevuss.weather.oracle.auth.validation.PasswordsMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchesValidator implements ConstraintValidator<PasswordsMatches, UserDto> {

    @Override
    public boolean isValid(UserDto value, ConstraintValidatorContext context) {
        if (value.getRawPassword() == null) return false;
        return value.getRawPassword().equals(value.getConfirmRawPassword());
    }
}
