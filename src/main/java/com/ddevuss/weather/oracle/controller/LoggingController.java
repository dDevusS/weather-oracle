package com.ddevuss.weather.oracle.controller;

import com.ddevuss.weather.oracle.dto.UserCreateDto;
import com.ddevuss.weather.oracle.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@AllArgsConstructor
@Controller
public class LoggingController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "authentication/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("userLogin") String userLogin, Model model) {
        model.addAttribute("userLogin", userLogin);
        return "authentication/registration";
    }

    @PostMapping("/registration")
    public String registration(@Validated UserCreateDto user,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userLogin", user.getLogin());
            redirectAttributes.addFlashAttribute("errorMessages", bindingResult.getAllErrors());
            return "redirect:/registration";
        }

        userService.save(user);
        log.info("New user with login '{}' was created", user.getLogin());
        return "redirect:/login";
    }

}
