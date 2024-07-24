package com.ddevuss.weather.oracle.dto;

import com.ddevuss.weather.oracle.validation.PasswordsMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@PasswordsMatches
public class UserCreateDto {

    @NotBlank(message = "Login must not be blank.")
    @Size(min = 3, max = 20, message = "Login size must be from 3 to 20 letters.")
    @Pattern(regexp = "^[\\S]+$", message = "Login must not contain spaces")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/]+$", message = "Login must contain only Latin letters and common symbols")
    String login;

    @NotBlank(message = "Password must not be blank.")
    @Pattern(regexp = "^[\\S]+$", message = "Password must not contain spaces")
    @Pattern(regexp = "^(?=.*\\d).{6,}$", message = "Password must be at least 6 characters long and contain at least one digit")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/]+$", message = "Password must contain only Latin letters and common symbols")
    String rawPassword;

    String confirmRawPassword;
}
