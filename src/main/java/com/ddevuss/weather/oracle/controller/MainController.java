package com.ddevuss.weather.oracle.controller;

import com.ddevuss.weather.oracle.dto.LocationResponseDto;
import com.ddevuss.weather.oracle.dto.UserInfoDto;
import com.ddevuss.weather.oracle.dto.UserSessionInfoDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.service.LocationService;
import com.ddevuss.weather.oracle.service.MainService;
import com.ddevuss.weather.oracle.service.OpenWeatherService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@SessionAttributes({
        "userInfo"
})
@Controller("/")
public class MainController {

    private final MainService mainService;
    private final LocationService locationService;
    private final OpenWeatherService openWeatherService;

    @GetMapping
    public String mainPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!"anonymousUser".equals(authentication.getName())) {
            String login = authentication.getName();
            UserInfoDto userInfo = mainService.getUserInfoByLogin(login);

            model.addAttribute("forecasts", userInfo.getForecasts());
            model.addAttribute("userInfo",
                    UserSessionInfoDto.builder()
                            .id(userInfo.getId())
                            .login(userInfo.getLogin())
                            .build());
        }

        return "main";
    }

    @GetMapping("location/search")
    public String searchLocation(Model model,
                                 @RequestParam String locationName) {
        LocationResponseDto[] locations = openWeatherService.searchLocationByName(locationName);
        model.addAttribute("locations", locations);
        return "searching";
    }

    @PostMapping("location/save")
    public String saveLocation(@ModelAttribute("location") LocationResponseDto locationResponseDto,
                               @SessionAttribute("userInfo") UserSessionInfoDto userInfo) {
        //TODO: checking for authorization for operation

        locationService.save(Location.builder()
                .user(User.builder()
                        .id(userInfo.getId())
                        .build())
                .name(locationResponseDto.getName())
                .latitude(locationResponseDto.getLat())
                .longitude(locationResponseDto.getLon())
                .build());

        return "redirect:/";
    }

    @PostMapping("location/delete")
    public String deleteLocation(Long locationId) {
        //TODO: checking for authorization for operation

        locationService.deleteById(locationId);
        return "redirect:/";
    }
}
