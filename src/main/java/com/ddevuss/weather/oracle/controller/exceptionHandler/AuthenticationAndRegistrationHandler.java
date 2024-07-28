package com.ddevuss.weather.oracle.controller.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Order(1)
@ControllerAdvice
public class AuthenticationAndRegistrationHandler {

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public String handleDataIntegrityViolationException(DataIntegrityViolationException e,
                                                        RedirectAttributes redirectAttributes,
                                                        Model model) {
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
        String constraintName = constraintViolationException.getConstraintName();

        if ("users_login_key".equals(constraintName)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "User with this login already exists");
            return "redirect:/registration";
        }
        else {
            log.error(e.getMessage(), e);

            model.addAttribute("errorMessage",
                    "There is unexpected error. Please try again later.");
            return "error";
        }
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public String handleBadCredentialsException(BadCredentialsException e,
                                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage",
                "Invalid login or password.");
        return "redirect:/login";
    }

    @ExceptionHandler(value = {AuthenticationServiceException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAuthenticationServiceException(AuthenticationServiceException e,
                                                       RedirectAttributes redirectAttributes) {
        log.error("AuthenticationServiceException", e);
        redirectAttributes.addFlashAttribute("errorMessage",
                "Something went wrong during authentication due to an error in the DB. Please try again later.");
        return "redirect:/login";
    }

}
