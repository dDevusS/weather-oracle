package com.ddevuss.weather.oracle.controller;

import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.dto.api.LocationApiResponseDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.exception.NotUniqueLocationException;
import com.ddevuss.weather.oracle.service.LocationService;
import com.ddevuss.weather.oracle.service.OpenWeatherService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@Controller
public class MainController {

    private final LocationService locationService;
    private final OpenWeatherService openWeatherService;

    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/forecast";
    }

    @GetMapping("/forecast")
    public String mainPage(Model model,
                           @RequestParam(required = false) Integer page,
                           @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String login = userDetails.getUsername();
            Slice<LocationReadDto> sliceLocations;

            if (page == null) {
                sliceLocations = locationService.findAllByUserLogin(login, 0);
            }
            else {
                sliceLocations = locationService.findAllByUserLogin(login, page);
            }

            model.addAttribute("forecasts",
                    openWeatherService.getWeatherForecast(sliceLocations.getContent()));
            model.addAttribute("slice", sliceLocations);
        }

        return "main";
    }

    @GetMapping("/location/search")
    public String searchLocation(Model model,
                                 @RequestParam(required = false) String locationName,
                                 @RequestParam(required = false) String errorMessage) {
        if (locationName == null || locationName.isBlank() || locationName.length() <= 2) {
            model.addAttribute("wrongParameterWarning",
                    "Name of location should not be blank or contain at least two characters.");
            return "searching";
        }

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("locationName", locationName);
        locationName = locationName.trim();
        LocationApiResponseDto[] locations = openWeatherService.searchLocationsByName(locationName);
        model.addAttribute("locations", locations);

        if (locations.length == 0) {
            model.addAttribute("noFoundMessage", "No location found with this name.");
        }

        return "searching";
    }

    @SneakyThrows
    @PostMapping("/location/save")
    public String saveLocation(Model model,
                               @ModelAttribute("locationName") String locationName,
                               @ModelAttribute("location") LocationApiResponseDto locationResponseDto,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            locationService.save(Location.builder()
                    .user(User.builder()
                            .login(userDetails.getUsername())
                            .build())
                    .name(locationResponseDto.getName())
                    .state(locationResponseDto.getState())
                    .latitude(locationResponseDto.getLat())
                    .longitude(locationResponseDto.getLon())
                    .build());
        }
        catch (NotUniqueLocationException e) {
            redirectAttributes.addFlashAttribute("duplicatedLocation", locationResponseDto);
            String encodedLocationName = URLEncoder.encode(locationName, StandardCharsets.UTF_8);
            String encodedErrorMessage = URLEncoder.encode("This location already exists", StandardCharsets.UTF_8);
            return "redirect:/location/search?locationName=" + encodedLocationName + "&errorMessage=" + encodedErrorMessage;
        }

        return "redirect:/forecast";
    }

    @PostMapping("/location/delete")
    public String deleteLocation(Long locationId, Integer numberObjects, Integer page) {
        if (numberObjects == 1 && page != 0) {
            page--;
        }
        locationService.deleteById(locationId);
        return "redirect:/forecast?page=" + page;
    }
}
