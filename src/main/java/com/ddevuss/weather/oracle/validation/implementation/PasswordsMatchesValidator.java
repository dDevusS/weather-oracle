package com.ddevuss.weather.oracle.validation.implementation;

import com.ddevuss.weather.oracle.dto.UserDto;
import com.ddevuss.weather.oracle.validation.PasswordsMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchesValidator implements ConstraintValidator<PasswordsMatches, UserDto> {

    @Override
    public boolean isValid(UserDto value, ConstraintValidatorContext context) {
        if (value.getRawPassword() == null) return false;
        return value.getRawPassword().equals(value.getConfirmRawPassword());
    }
}
