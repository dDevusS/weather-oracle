package com.ddevuss.weather.oracle.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient openWeatherRestClient(WeatherOracleConfiguration properties) {
        return RestClient.builder().baseUrl(properties.getOpenWeatherApi().url()).build();
    }
}
