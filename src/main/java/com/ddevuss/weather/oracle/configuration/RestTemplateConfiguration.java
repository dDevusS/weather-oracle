package com.ddevuss.weather.oracle.configuration;

import com.ddevuss.weather.oracle.handler.RestTemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @Autowired
    public RestTemplate restTemplate(RestTemplateExceptionHandler restTemplateExceptionHandler) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(restTemplateExceptionHandler);
        return restTemplate;
    }
}
