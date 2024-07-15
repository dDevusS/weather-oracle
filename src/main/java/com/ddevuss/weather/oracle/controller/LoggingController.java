package com.ddevuss.weather.oracle.controller;

import com.ddevuss.weather.oracle.dto.UserCreateDto;
import com.ddevuss.weather.oracle.exception.LoginNotUniqueException;
import com.ddevuss.weather.oracle.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@AllArgsConstructor
@Controller
public class LoggingController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "/authentication/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("userLogin") String userLogin, Model model) {
        model.addAttribute("userLogin", userLogin);
        return "/authentication/registration";
    }

    @PostMapping("/registration")
    public String registration(UserCreateDto user, String confirmRawPassword, RedirectAttributes redirectAttributes) {
        if (!confirmRawPassword.equals(user.getRawPassword())) {
            redirectAttributes.addFlashAttribute("userLogin", user.getLogin());
            //TODO: use BindingResult . ?
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match");
            return "redirect:/registration";
        }

        try {
            userService.save(user);
        }
        catch (LoginNotUniqueException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/registration";
        }

        return "redirect:/login";

//        if (result.hasErrors()) {
//            return "authentication/registration";
//        }
//
//        if (!confirmRawPassword.equals(user.getPassword())) {
//            result.rejectValue("password", "error.user", "Passwords do not match");
//            return "authentication/registration";
//        }
//
//        try {
//            userService.registerUser(user);
//            redirectAttributes.addFlashAttribute("userLogin", user.getUsername());
//            return "redirect:/login";
//        } catch (UsernameAlreadyExistsException e) {
//            result.rejectValue("username", "error.user", "Username already exists");
//            return "authentication/registration";
//        }
    }

}
