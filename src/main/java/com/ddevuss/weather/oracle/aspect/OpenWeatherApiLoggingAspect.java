package com.ddevuss.weather.oracle.aspect;

import com.ddevuss.weather.oracle.dto.LocationReadDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Aspect
@Component
@Slf4j
public class OpenWeatherApiLoggingAspect {

    private static final String TEMPLATE_URL_GEO_API = "http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&limit={limit}&appid={API key}";
    private static final String TEMPLATE_URL_WEATHER_API = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}";

    private static final Map<String, String> METHOD_TO_TEMPLATE_URL = Map.of(
            "searchLocationsByName", TEMPLATE_URL_GEO_API,
            "getWeatherForecast", TEMPLATE_URL_WEATHER_API
    );

    @Pointcut("within(com.ddevuss.weather.oracle.service.OpenWeatherService)")
    public void isOpenWeatherService() {
    }

    @Before("isOpenWeatherService() " +
            "&& args(locationName)")
    public void logOpenWeatherApiService(String locationName) {
        log.info("Sending request to Open Weather API for searching location with name {}", locationName);
    }

    @Before("isOpenWeatherService() " +
            "&& args(locations)")
    public void logOpenWeatherApiService(List<LocationReadDto> locations) {
        log.info("Sending requests to Open Weather API to get weather forecast for {} locations", locations.size());
    }

    @AfterThrowing(value = "isOpenWeatherService()", throwing = "exception")
    public void logOpenWeatherApiService(JoinPoint joinPoint, RestClientResponseException exception) {
        logTemplateUrl(joinPoint.getSignature().getName());
        logException(exception);
    }

    private void logException(RestClientResponseException exception) {
        String message = "{}: {}";
        String description = getExceptionDescription(exception);

        if (TOO_MANY_REQUESTS == exception.getStatusCode()) {
            log.warn(message, exception.getClass().getSimpleName(), description);
        }
        else {
            log.error(message, exception.getClass().getSimpleName(), description);
        }
    }

    @After("isOpenWeatherService()")
    public void logOpenWeatherApiService(JoinPoint joinPoint) {
        log.info("Request to Open Weather API for {} method has been processed successfully", joinPoint.getSignature().getName());
    }

    private void logTemplateUrl(String methodName) {
        String templateUrl = METHOD_TO_TEMPLATE_URL.get(methodName);

        if (templateUrl != null) {
            log.error("Template URL for method {}: {}", methodName, templateUrl);
        }
        else {
            log.error("No template URL defined for method: {}", methodName);
        }
    }

    private String getExceptionDescription(RestClientResponseException exception) {
        HttpStatusCode statusCode = exception.getStatusCode();
        if (BAD_REQUEST == statusCode) {
            return statusCode.value() + " - Bad request exception to API server";
        }
        else if (NOT_FOUND == statusCode) {
            return statusCode.value() + " - Not found exception from API server";
        }
        else if (UNAUTHORIZED == statusCode) {
            return statusCode.value() + " - Unauthorized exception from API server";
        }
        else if (TOO_MANY_REQUESTS == statusCode) {
            return statusCode.value() + " - 429 Too many requests exception to API server";
        }
        else if (statusCode.is5xxServerError()) {
            return statusCode.value() + " - 5xx Server error exception from API server";
        }
        else {
            return statusCode.value() + " - Unknown exception from API server";
        }
    }

}
