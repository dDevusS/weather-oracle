package com.ddevuss.weather.oracle.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller("/")
public class MainController {

    @GetMapping
    public String mainPage() {
        return "main";
    }
}
