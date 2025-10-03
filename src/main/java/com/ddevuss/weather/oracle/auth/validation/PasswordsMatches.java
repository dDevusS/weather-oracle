package com.ddevuss.weather.oracle.auth.validation;

import com.ddevuss.weather.oracle.auth.validation.implementation.PasswordsMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordsMatchesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordsMatches {

    String message() default "Passwords have to be matched.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
