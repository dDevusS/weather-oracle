package com.ddevuss.weather.oracle.auth.validation.implementation;

import com.ddevuss.weather.oracle.auth.dto.UserCreateDto;
import com.ddevuss.weather.oracle.auth.validation.PasswordsMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchesValidator implements ConstraintValidator<PasswordsMatches, UserCreateDto> {

    @Override
    public boolean isValid(UserCreateDto value, ConstraintValidatorContext context) {
        if (value.getRawPassword() == null) return false;
        return value.getRawPassword().equals(value.getConfirmRawPassword());
    }
}
