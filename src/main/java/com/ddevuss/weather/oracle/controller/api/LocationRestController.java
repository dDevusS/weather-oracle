package com.ddevuss.weather.oracle.controller.api;

import com.ddevuss.weather.oracle.controller.api.docs.LocationController;
import com.ddevuss.weather.oracle.dto.LocationDto;
import com.ddevuss.weather.oracle.service.LocationService;
import com.ddevuss.weather.oracle.service.OpenWeatherService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api")
public class LocationRestController implements LocationController {

    private final LocationService locationService;
    private final OpenWeatherService openWeatherService;
    private static final int MIN_SIZE_NAME_FOR_SEARCH = 3;

    @Validated
    @GetMapping("/locations/search")
    public ResponseEntity<List<LocationDto>> searchLocationsByName(
            @RequestParam
            @NotBlank(message = "Location name should be not blank")
            @Size(min = MIN_SIZE_NAME_FOR_SEARCH, message = "Location name should contain at least {min} characters")
            String locationName) {
        locationName = locationName.trim();
        List<LocationDto> locations = openWeatherService.searchLocationsByName(locationName);
        return ResponseEntity.ok(locations);
    }

    @PostMapping("/location/save")
    public ResponseEntity<Void> saveLocation(@RequestBody @Valid LocationDto locationDto, Principal principal) {
        locationService.save(locationDto, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @Validated
    @DeleteMapping("/location/{id}")
    public ResponseEntity<Void> deleteLocation(@NotNull @PositiveOrZero @PathVariable("id") Long locationId) {
        locationService.deleteById(locationId);
        return ResponseEntity.noContent().build();
    }

    @Validated
    @GetMapping("locations")
    public ResponseEntity<Slice<LocationDto>> getLocations(@PositiveOrZero @RequestParam(required = false) Integer pageNumber, Principal principal) {
        Slice<LocationDto> locations = locationService.findAllByUserLogin(principal.getName(), pageNumber);
        return ResponseEntity.ok(locations);
    }

}
