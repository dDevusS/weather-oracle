package com.ddevuss.weather.oracle.controller.exceptionHandler;

import com.ddevuss.weather.oracle.exception.LoginNotUniqueException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class AuthenticationAndRegistrationHandler {

    @ExceptionHandler(value = {LoginNotUniqueException.class})
    public String handleLoginNotUniqueException(LoginNotUniqueException e,
                                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/registration";
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public String handleBadCredentialsException(BadCredentialsException e,
                                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage",
                "Invalid login or password.");
        return "redirect:/login";
    }

    @ExceptionHandler(value = {AuthenticationServiceException.class})
    public String handleAuthenticationServiceException(AuthenticationServiceException e,
                                                       RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage",
                "Something went wrong during authentication due to an error in the DB. Please try again later.");
        return "redirect:/login";
    }

}
