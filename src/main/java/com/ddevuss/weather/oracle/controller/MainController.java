package com.ddevuss.weather.oracle.controller;

import com.ddevuss.weather.oracle.dto.UserInfoDto;
import com.ddevuss.weather.oracle.dto.UserSessionInfoDto;
import com.ddevuss.weather.oracle.dto.api.LocationResponseDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.exception.DuplicatedLocationException;
import com.ddevuss.weather.oracle.service.LocationService;
import com.ddevuss.weather.oracle.service.MainService;
import com.ddevuss.weather.oracle.service.OpenWeatherService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
                                 @RequestParam(required = false) String locationName,
                                 @RequestParam(required = false) String errorMessage) {
        if (locationName == null || locationName.isBlank()) {
            model.addAttribute("blankParameterWarning",
                    "Name of location was empty.");
            return "searching";
        }

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("locationName", locationName);
        locationName = locationName.trim();
        LocationResponseDto[] locations = openWeatherService.searchLocationsByName(locationName);
        model.addAttribute("locations", locations);
        return "searching";
    }

    @SneakyThrows
    @PostMapping("location/save")
    public String saveLocation(Model model,
                               @ModelAttribute("locationName") String locationName,
                               @ModelAttribute("location") LocationResponseDto locationResponseDto,
                               @SessionAttribute("userInfo") UserSessionInfoDto userInfo) {
        try {
            locationService.save(Location.builder()
                    .user(User.builder()
                            .id(userInfo.getId())
                            .login(userInfo.getLogin())
                            .build())
                    .name(locationResponseDto.getName())
                    .latitude(locationResponseDto.getLat())
                    .longitude(locationResponseDto.getLon())
                    .build());
        }
        catch (DuplicatedLocationException e) {
            String encodedLocationName = URLEncoder.encode(locationName, StandardCharsets.UTF_8);
            String encodedErrorMessage = URLEncoder.encode("This location already exists", StandardCharsets.UTF_8);
            return "redirect:/location/search?locationName=" + encodedLocationName + "&errorMessage=" + encodedErrorMessage;
        }

        return "redirect:/";
    }

    @PostMapping("location/delete")
    public String deleteLocation(Long locationId) {
        locationService.deleteById(locationId);
        return "redirect:/";
    }
}
