package com.ddevuss.weather.oracle.aspect;

import com.ddevuss.weather.oracle.configuration.application.OpenWeatherApiProperties;
import com.ddevuss.weather.oracle.dto.LocationDto;
import com.ddevuss.weather.oracle.utils.OpenWeatherUrlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
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

    private final Map<String, String> methodToTemplateUrl;
    private static final String MESSAGE_PATTERN = "{}: {}";

    public OpenWeatherApiLoggingAspect(OpenWeatherApiProperties configuration) {
        String openWeatherApiUrl = configuration.url();
        methodToTemplateUrl = Map.of(
                "searchLocationsByName", openWeatherApiUrl + OpenWeatherUrlBuilder.buildUrlForGeoApi("{locationName}", "{appId}"),
                "getWeatherForecast", openWeatherApiUrl + OpenWeatherUrlBuilder.buildUrlForWeatherApi(0.01, 0.01, "{appId}")
        );
    }

    @Pointcut("within(com.ddevuss.weather.oracle.controller.exceptionHandler.ApiProblemHandler)")
    public void isApiProblemHandler() {}

    @Pointcut("execution(* handleRestClientResponseException(..))")
    public void isHandleRestClientResponseException() {}

    @Pointcut("execution(* handleRestClientException(..))")
    public void isHandleRestClientException() {}

    @Pointcut("within(com.ddevuss.weather.oracle.service.OpenWeatherService)")
    public void isOpenWeatherService() {
    }

    @Before("isApiProblemHandler() " +
            "&& isHandleRestClientException()" +
            "&& args(clientException, ..)")
    public void logRestClientException(RestClientException clientException) {
        if (clientException instanceof RestClientResponseException responseException) {
            logRestClientResponseException(responseException);
        }
        else {
            String exceptionName = clientException.getClass().getSimpleName();
            log.error(MESSAGE_PATTERN, exceptionName, clientException.getMessage());
        }
    }

    @Before("isOpenWeatherService() " +
            "&& args(locationName)")
    public void logOpenWeatherApiService(String locationName) {
        log.info("Sending request to Open Weather API for searching location with name {}", locationName);
    }

    @Before("isOpenWeatherService() " +
            "&& args(locations)")
    public void logOpenWeatherApiService(List<LocationDto> locations) {
        log.info("Sending requests to Open Weather API to get weather forecast for {} locations", locations.size());
    }

    @AfterThrowing(value = "isOpenWeatherService()", throwing = "exception")
    public void logOpenWeatherApiService(JoinPoint joinPoint, Throwable exception) {
        logTemplateUrl(joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "isOpenWeatherService()", returning = "result")
    public void logOpenWeatherApiService(JoinPoint joinPoint, Object result) {
        log.info("Request to Open Weather API for {} method has been processed successfully", joinPoint.getSignature().getName());
    }

    private void logRestClientResponseException(RestClientResponseException responseException) {
        HttpStatusCode statusCode = responseException.getStatusCode();
        String exceptionName = responseException.getClass().getSimpleName();

        if (BAD_REQUEST == statusCode) {
            String message = statusCode.value() + " - Bad request exception to API server";
            log.error(MESSAGE_PATTERN, exceptionName, message);
        }
        else if (NOT_FOUND == statusCode) {
            String message = statusCode.value() + " - Not found exception from API server";
            log.error(MESSAGE_PATTERN, exceptionName, message);
        }
        else if (UNAUTHORIZED == statusCode) {
            String message = statusCode.value() + " - Unauthorized exception from API server";
            log.error(MESSAGE_PATTERN, exceptionName, message);
        }
        else if (TOO_MANY_REQUESTS == statusCode) {
            String message = statusCode.value() + " - 429 Too many requests exception to API server";
            log.warn(MESSAGE_PATTERN, exceptionName, message);
        }
        else if (statusCode.is5xxServerError()) {
            String message = statusCode.value() + " - 5xx Server error exception from API server";
            log.error(MESSAGE_PATTERN, exceptionName, message);
        }
        else {
            String message = statusCode.value() + " - Unknown exception from API server";
            log.error(MESSAGE_PATTERN, exceptionName, message);
        }
    }

    private void logTemplateUrl(String methodName) {
        String templateUrl = methodToTemplateUrl.get(methodName);

        if (templateUrl != null) {
            log.error("Template URL for method {}: {}", methodName, templateUrl);
        }
        else {
            log.error("No template URL defined for method: {}", methodName);
        }
    }

}
