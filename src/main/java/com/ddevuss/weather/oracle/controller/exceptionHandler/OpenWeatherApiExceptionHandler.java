package com.ddevuss.weather.oracle.controller.exceptionHandler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Order(2)
@ControllerAdvice
public class OpenWeatherApiExceptionHandler {

    @ExceptionHandler(value = RestClientResponseException.class)
    public String handleBadRequestApiServerException(RestClientResponseException exception,
                                                     Model model,
                                                     HttpServletResponse response) {
        HttpStatusCode statusCode = exception.getStatusCode();

        if (BAD_REQUEST == statusCode || NOT_FOUND == statusCode || UNAUTHORIZED == statusCode) {
            model.addAttribute("errorMessage",
                    "Something went wrong on the server. Please try again later.");
            response.setStatus(INTERNAL_SERVER_ERROR.value());
        }
        else if (TOO_MANY_REQUESTS == statusCode) {
            model.addAttribute("errorMessage",
                    "Unfortunately, quota has been reached. Please, wait a minute and try again.");
            response.setStatus(TOO_MANY_REQUESTS.value());
        }
        else {
            model.addAttribute("errorMessage",
                    "Something went wrong due to error on API server provided by openweathermap.org Please try again later.");
            response.setStatus(INTERNAL_SERVER_ERROR.value());
        }

        return "error";
    }

}
