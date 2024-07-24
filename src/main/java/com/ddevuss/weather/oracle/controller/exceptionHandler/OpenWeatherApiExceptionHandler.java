package com.ddevuss.weather.oracle.controller.exceptionHandler;

import com.ddevuss.weather.oracle.exception.api.ApiServerErrorException;
import com.ddevuss.weather.oracle.exception.api.BadRequestApiServerException;
import com.ddevuss.weather.oracle.exception.api.QuotaApiFinishException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Order(2)
@ControllerAdvice
public class OpenWeatherApiExceptionHandler {

    @ExceptionHandler(value = BadRequestApiServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleBadRequestApiServerException(Model model) {
        model.addAttribute("errorMessage",
                "Something went wrong on the server. Please try again later.");
        return "error";
    }

    @ExceptionHandler(value = QuotaApiFinishException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public String handleQuotaFinishException(Model model) {
        model.addAttribute("errorMessage",
                "Unfortunately, quota has been reached. Please, wait a minute and try again.");
        return "error";
    }

    @ExceptionHandler(value = ApiServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleApiServerErrorException(Model model) {
        model.addAttribute("errorMessage",
                "Something went wrong due to error on API server provided by openweathermap.org Please try again later.");
        return "error";
    }

}
