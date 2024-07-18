package com.ddevuss.weather.oracle.controller.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Order(3)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception e) {
        log.error(e.getMessage(), e);

        model.addAttribute("errorMessage",
                "There was unexpected error. Please try again later.");
        return "error";
    }
}
