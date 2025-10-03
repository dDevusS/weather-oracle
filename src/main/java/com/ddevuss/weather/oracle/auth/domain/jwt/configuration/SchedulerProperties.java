package com.ddevuss.weather.oracle.auth.domain.jwt.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "application.jwt.cleanup",  ignoreUnknownFields = false)
public record SchedulerProperties(@DefaultValue("0 0 0/12 * * *") @NotEmpty String schedule,
                                  @DefaultValue("UTC") @NotEmpty String zone) {

}
