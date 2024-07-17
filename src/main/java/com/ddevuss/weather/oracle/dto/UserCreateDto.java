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
    String login;

    @NotBlank(message = "Password must not be blank.")
    @Pattern(regexp = "^(?=.*\\d).{6,}$", message = "Password must be at least 6 characters long and contain at least one digit")
    String rawPassword;

    String confirmRawPassword;
}
