package com.ddevuss.weather.oracle.dto;

import com.ddevuss.weather.oracle.validation.PasswordsMatches;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@PasswordsMatches
public class UserDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    Long id;

    @NotBlank(message = "Login must not be blank.")
    @Size(min = 3, max = 20, message = "Login size must be from 3 to 20 letters.")
    @Pattern(regexp = "^[\\S]+$", message = "Login must not contain spaces")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/]+$", message = "Login must contain only Latin letters and common symbols")
    String login;

    @NotBlank(message = "Password must not be blank.")
    @Pattern(regexp = "^[\\S]+$", message = "Password must not contain spaces")
    @Pattern(regexp = "^(?=.*\\d).{6,}$", message = "Password must be at least 6 characters long and contain at least one digit")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/]+$", message = "Password must contain only Latin letters and common symbols")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String rawPassword;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String confirmRawPassword;

}
