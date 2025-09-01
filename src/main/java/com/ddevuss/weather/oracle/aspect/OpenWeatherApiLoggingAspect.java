package com.ddevuss.weather.oracle.aspect;

import com.ddevuss.weather.oracle.configuration.WeatherOracleConfiguration;
import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.service.OpenWeatherService;
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

    public OpenWeatherApiLoggingAspect(WeatherOracleConfiguration configuration) {
        String openWeatherApiUri = configuration.getOpenWeatherApi().url();
        methodToTemplateUrl = Map.of(
                "searchLocationsByName", openWeatherApiUri + OpenWeatherService.buildUrlForGeoApi("{locationName}", "{appId}"),
                "getWeatherForecast", openWeatherApiUri + OpenWeatherService.buildUrlForWeatherApi(0.01, 0.01, "{appId}")
        );
    }

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
    public void logOpenWeatherApiService(JoinPoint joinPoint, Throwable exception) {
        logTemplateUrl(joinPoint.getSignature().getName());
        logException(exception);
    }

    private void logException(Throwable exception) {
        String message = "{}: {}";

        if (exception instanceof RestClientException responseException) {
            String description = getExceptionDescription(responseException);

            if (description.startsWith(Integer.toString(TOO_MANY_REQUESTS.value()))) {
                log.warn(message, responseException.getClass().getSimpleName(), description);
            }
            else {
                log.error(message, exception.getClass().getSimpleName(), description);
            }
        }
        else {
            log.error("Unexpected exception: {}", exception.toString(), exception);
        }
    }

    @AfterReturning(value = "isOpenWeatherService()", returning = "result")
    public void logOpenWeatherApiService(JoinPoint joinPoint, Object result) {
        log.info("Request to Open Weather API for {} method has been processed successfully", joinPoint.getSignature().getName());
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

    private String getExceptionDescription(RestClientException exception) {
        if (exception instanceof RestClientResponseException responseException) {
            HttpStatusCode statusCode = responseException.getStatusCode();
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
        else {
            return "Unknown exception: " + exception.getMessage();
        }
    }

}
