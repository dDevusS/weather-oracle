package com.ddevuss.weather.oracle.controller;

import com.ddevuss.weather.oracle.dto.api.LocationApiResponseDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.exception.NotUniqueLocationException;
import com.ddevuss.weather.oracle.service.LocationService;
import com.ddevuss.weather.oracle.service.OpenWeatherService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@Controller("/")
public class MainController {

    private final LocationService locationService;
    private final OpenWeatherService openWeatherService;

    @GetMapping
    public String mainPage(Model model) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!"anonymousUser".equals(login)) {
            var locations = locationService.findAllByUserLogin(login);
            model.addAttribute("forecasts",
                    openWeatherService.getWeatherForecast(locations));
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
        LocationApiResponseDto[] locations = openWeatherService.searchLocationsByName(locationName);
        model.addAttribute("locations", locations);
        return "searching";
    }

    @SneakyThrows
    @PostMapping("location/save")
    public String saveLocation(Model model,
                               @ModelAttribute("locationName") String locationName,
                               @ModelAttribute("location") LocationApiResponseDto locationResponseDto) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            locationService.save(Location.builder()
                    .user(User.builder()
                            .login(login)
                            .build())
                    .name(locationResponseDto.getName())
                    .latitude(locationResponseDto.getLat())
                    .longitude(locationResponseDto.getLon())
                    .build());
        }
        catch (NotUniqueLocationException e) {
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
