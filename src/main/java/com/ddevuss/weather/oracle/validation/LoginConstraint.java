package com.ddevuss.weather.oracle.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

@NotBlank(message = "{login.not.blank}")
@Size(min = 3, max = 20, message = "{login.size.constraint}")
@Pattern(regexp = "^[\\S]+$", message = "{Login.without.spaces}")
@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/]+$", message = "{login.symbols.constraint}")
public @interface LoginConstraint {

    String message() default "Invalid login.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
