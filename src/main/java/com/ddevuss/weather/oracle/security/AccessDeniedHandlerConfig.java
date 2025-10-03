package com.ddevuss.weather.oracle.security;

import com.ddevuss.weather.oracle.common.web.ProblemDetailBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.security.web.access.AccessDeniedHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@Configuration
class AccessDeniedHandlerConfig {

    private final MessageSource messageSource;

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper mapper) {
        return (request, response, exception) -> {
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            ProblemDetail pd = ProblemDetailBuilder.forStatus(FORBIDDEN)
                    .title(messageSource.getMessage("error.forbidden", null, request.getLocale()))
                    .uri(request)
                    .build();

            mapper.writeValue(response.getOutputStream(), pd);
        };
    }
}
