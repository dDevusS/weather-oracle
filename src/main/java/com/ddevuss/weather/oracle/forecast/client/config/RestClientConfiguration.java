package com.ddevuss.weather.oracle.forecast.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient openWeatherRestClient(OpenWeatherApiProperties properties,
                                            ClientHttpRequestInterceptor openWeatherLoggingInterceptor) {
        return RestClient.builder()
                .baseUrl(properties.url())
                .requestInterceptor(openWeatherLoggingInterceptor)
                .build();
    }

}
