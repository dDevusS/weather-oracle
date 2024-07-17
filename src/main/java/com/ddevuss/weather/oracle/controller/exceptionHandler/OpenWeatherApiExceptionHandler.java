package com.ddevuss.weather.oracle.controller.exceptionHandler;

import com.ddevuss.weather.oracle.exception.ApiServerErrorException;
import com.ddevuss.weather.oracle.exception.BadRequestApiServerException;
import com.ddevuss.weather.oracle.exception.QuotaFinishException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OpenWeatherApiExceptionHandler {

    @ExceptionHandler(value = BadRequestApiServerException.class)
    public String handleBadRequestApiServerException(Model model) {
        model.addAttribute("errorMessage",
                "Something went wrong on the server. Please try again later.");
        return "error";
    }

    @ExceptionHandler(value = QuotaFinishException.class)
    public String handleQuotaFinishException(Model model) {
        model.addAttribute("errorMessage",
                "Unfortunately, quota has been reached. Please, wait a minute and try again.");
        return "error";
    }

    @ExceptionHandler(value = ApiServerErrorException.class)
    public String handleApiServerErrorException(Model model) {
        model.addAttribute("errorMessage",
                "Something went wrong due to error on API server provided by openweathermap.org Please try again later.");
        return "error";
    }
}
