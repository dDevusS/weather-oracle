package com.ddevuss.weather.oracle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WeatherOracleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherOracleApplication.class, args);
    }

}
