package com.ddevuss.weather.oracle.security.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@Slf4j
@Configuration
public class LoggingAuthConfig {

    private static final String AUTH_ATTEMPT_MESSAGE_TEMPLATE = "Authentication attempt for user {}";
    private static final String AUTH_SUCCESS_MESSAGE_TEMPLATE = "Authentication success for user {}";
    private static final String AUTH_FAILED_MESSAGE_TEMPLATE = "Authentication failed for user {}";

    @Bean
    public ApplicationListener<AbstractAuthenticationEvent> authenticationLogger() {
        return event -> {
            log.atDebug()
                    .addArgument(event.getAuthentication().getName())
                    .log(AUTH_ATTEMPT_MESSAGE_TEMPLATE);
            if (event instanceof AuthenticationSuccessEvent success) {
                log.atDebug()
                        .addArgument(success.getAuthentication().getName())
                        .log(AUTH_SUCCESS_MESSAGE_TEMPLATE);
            }
            else if (event instanceof AbstractAuthenticationFailureEvent failure) {
                log.atDebug()
                        .addArgument(failure.getAuthentication().getName())
                        .log(AUTH_FAILED_MESSAGE_TEMPLATE);
            }
        };
    }
}
