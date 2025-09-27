package com.ddevuss.weather.oracle.dto;

import com.ddevuss.weather.oracle.validation.LoginConstraint;
import com.ddevuss.weather.oracle.validation.PasswordsMatches;
import com.ddevuss.weather.oracle.validation.StrongPassword;
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
public class UserDto implements Serializable {

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
