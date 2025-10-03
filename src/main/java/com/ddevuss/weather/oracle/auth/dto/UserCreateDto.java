package com.ddevuss.weather.oracle.auth.dto;

import com.ddevuss.weather.oracle.auth.validation.LoginConstraint;
import com.ddevuss.weather.oracle.auth.validation.PasswordsMatches;
import com.ddevuss.weather.oracle.auth.validation.StrongPassword;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserCreateDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    Long id;

    @LoginConstraint
    String login;

    @StrongPassword
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String rawPassword;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String confirmRawPassword;

}
