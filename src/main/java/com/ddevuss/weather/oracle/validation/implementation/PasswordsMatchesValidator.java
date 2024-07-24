package com.ddevuss.weather.oracle.validation.implementation;

import com.ddevuss.weather.oracle.dto.UserCreateDto;
import com.ddevuss.weather.oracle.validation.PasswordsMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchesValidator implements ConstraintValidator<PasswordsMatches, UserCreateDto> {

    @Override
    public boolean isValid(UserCreateDto value, ConstraintValidatorContext context) {
        return value.getRawPassword().equals(value.getConfirmRawPassword());
    }
}
