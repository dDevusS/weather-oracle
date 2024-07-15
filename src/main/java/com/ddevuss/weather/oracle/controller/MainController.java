package com.ddevuss.weather.oracle.controller;

import com.ddevuss.weather.oracle.dto.UserInfoDto;
import com.ddevuss.weather.oracle.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@AllArgsConstructor
@SessionAttributes({
        "userInfo"
})
@Controller("/")
public class MainController {

    private final UserService userService;

    @GetMapping
    public String mainPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!"anonymousUser".equals(authentication.getName())) {
            String login = authentication.getName();
            UserInfoDto userInfo = userService.getUserInfoByLogin(login);
            model.addAttribute("userInfo", userInfo);
        }
        return "main";
    }
}
