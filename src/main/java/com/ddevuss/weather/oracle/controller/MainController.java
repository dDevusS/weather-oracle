package com.ddevuss.weather.oracle.controller;

import com.ddevuss.weather.oracle.dto.LocationResponseDto;
import com.ddevuss.weather.oracle.dto.UserInfoDto;
import com.ddevuss.weather.oracle.dto.UserSessionInfoDto;
import com.ddevuss.weather.oracle.dto.WeatherForecastDto;
import com.ddevuss.weather.oracle.service.OpenWeatherService;
import com.ddevuss.weather.oracle.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@AllArgsConstructor
@SessionAttributes({
        "userInfo"
})
@Controller("/")
public class MainController {

    private final UserService userService;
    private final OpenWeatherService openWeatherService;

    @GetMapping
    public String mainPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!"anonymousUser".equals(authentication.getName())) {
            String login = authentication.getName();
            UserInfoDto userInfo = userService.getUserInfoByLogin(login);
            List<WeatherForecastDto> forecasts = openWeatherService.getWeatherForecast(userInfo.getLocations());

            model.addAttribute("forecasts", forecasts);
            model.addAttribute("userInfo",
                    UserSessionInfoDto.builder()
                            .id(userInfo.getId())
                            .login(userInfo.getLogin())
                            .build());
        }
        return "main";
    }

    @GetMapping("search")
    public String searchLocation(Model model,
                                 @RequestParam String locationName) {
        LocationResponseDto[] locations = openWeatherService.searchLocation(locationName);
        model.addAttribute("locations", locations);
        return "searching";
    }
}
