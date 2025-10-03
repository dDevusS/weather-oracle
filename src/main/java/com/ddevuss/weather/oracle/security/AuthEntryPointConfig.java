package com.ddevuss.weather.oracle.security;

import com.ddevuss.weather.oracle.common.web.ProblemDetailBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.security.web.AuthenticationEntryPoint;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@Configuration
class AuthEntryPointConfig {

    private final MessageSource messageSource;

    @Bean
    public AuthenticationEntryPoint restEntryPoint(ObjectMapper mapper) {
        return (request, response, exception) -> {
            response.setStatus(UNAUTHORIZED.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            ProblemDetail pd = ProblemDetailBuilder.forStatus(UNAUTHORIZED)
                    .title(messageSource.getMessage("error.unauthorized", null, request.getLocale()))
                    .uri(request)
                    .build();

            mapper.writeValue(response.getOutputStream(), pd);
        };
    }
}
