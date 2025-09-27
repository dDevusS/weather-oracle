package com.ddevuss.weather.oracle.configuration;

import com.ddevuss.weather.oracle.configuration.application.OpenWeatherApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient openWeatherRestClient(OpenWeatherApiProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.url())
                .build();
    }

}
