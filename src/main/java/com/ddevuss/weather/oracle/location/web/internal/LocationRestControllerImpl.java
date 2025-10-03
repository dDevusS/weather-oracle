package com.ddevuss.weather.oracle.location.web.internal;

import com.ddevuss.weather.oracle.forecast.client.OpenWeatherClient;
import com.ddevuss.weather.oracle.location.domain.LocationService;
import com.ddevuss.weather.oracle.location.dto.LocationDto;
import com.ddevuss.weather.oracle.location.web.doc.LocationRestController;
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

import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/locations")
public class LocationRestControllerImpl implements LocationRestController {

    private final LocationService locationService;
    private final OpenWeatherClient openWeatherClient;
    private static final int MIN_SIZE_NAME_FOR_SEARCH = 3;

    @GetMapping("/search")
    public ResponseEntity<List<LocationDto>> searchByName(
            @RequestParam
            @NotBlank(message = "{location.name.not.blank}")
            @Size(min = MIN_SIZE_NAME_FOR_SEARCH, message = "{location.name.size.constraint}")
            String locationName) {
        List<LocationDto> locations = openWeatherClient.searchLocationByName(locationName);
        return ResponseEntity.ok(locations);
    }

    @PostMapping
    public ResponseEntity<LocationDto> save(@RequestBody @Valid LocationDto locationDto, Principal principal) {
        LocationDto savedLocation = locationService.save(locationDto, principal.getName());
        return ResponseEntity.status(CREATED).body(savedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@NotNull @PositiveOrZero @PathVariable("id") Long locationId) {
        locationService.deleteById(locationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Slice<LocationDto>> get(@PositiveOrZero @RequestParam(required = false) Integer pageNumber, Principal principal) {
        Slice<LocationDto> locations = locationService.findAllByUserLogin(principal.getName(), pageNumber);
        return ResponseEntity.ok(locations);
    }

}
