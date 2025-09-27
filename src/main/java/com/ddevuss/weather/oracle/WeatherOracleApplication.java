package com.ddevuss.weather.oracle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.ddevuss.weather.oracle.configuration")
@EnableScheduling
public class WeatherOracleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherOracleApplication.class, args);
    }

}
