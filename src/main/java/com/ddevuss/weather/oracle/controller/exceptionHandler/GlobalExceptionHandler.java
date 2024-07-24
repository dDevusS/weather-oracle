package com.ddevuss.weather.oracle.controller.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@Order(3)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException(Model model) {
        model.addAttribute("errorMessage",
                "404 - There is no resource available for this URL.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleAnyException(Model model, Exception e) {
        log.error(e.getMessage(), e);

        model.addAttribute("errorMessage",
                "There was unexpected error. Please try again later.");
        return "error";
    }
}
