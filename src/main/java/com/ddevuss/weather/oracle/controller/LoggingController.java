package com.ddevuss.weather.oracle.controller;

import com.ddevuss.weather.oracle.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class LoggingController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "/authentication/login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "/authentication/registration";
    }

}
