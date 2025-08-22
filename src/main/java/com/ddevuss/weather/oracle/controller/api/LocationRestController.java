package com.ddevuss.weather.oracle.controller.api;

import com.ddevuss.weather.oracle.controller.api.docs.LocationController;
import com.ddevuss.weather.oracle.dto.ApiErrorDto;
import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.dto.api.LocationApiResponseDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.service.LocationService;
import com.ddevuss.weather.oracle.service.OpenWeatherService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class LocationRestController implements LocationController {

    private final LocationService locationService;
    private final OpenWeatherService openWeatherService;

    @GetMapping("/locations/search")
    public ResponseEntity<?> searchLocationsByName(@RequestParam String locationName) {
        return Optional.ofNullable(locationName)
                .filter(Predicate.not(String::isBlank))
                .map(String::trim)
                .filter(location -> location.length() > 2)
                .map(location -> {
                    List<LocationApiResponseDto> locations;
                    try {
                        locations = openWeatherService.searchLocationsByName(location);

                        return !locations.isEmpty()
                                ? ResponseEntity.ok(locations)
                                : ResponseEntity.notFound().build();
                    }
                    catch (RestClientResponseException ex) {
                        return ResponseEntity.internalServerError().body(new ApiErrorDto("INTERNAL_ERROR", "Something went wrong", "INTERNAL"));
                    }
                })
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body(new ApiErrorDto("", "Location name should not be blank and must contain at least three characters.", "VALIDATION_ERROR")));
    }

    @PostMapping("/location/save")
    public ResponseEntity<?> saveLocation(@RequestBody @Validated LocationApiResponseDto locationApiResponseDto, Principal principal) {
        try {
            locationService.save(
                    Location.builder()
                            .user(User.builder()
                                    .login(principal.getName())
                                    .build())
                            .name(locationApiResponseDto.getName())
                            .state(Optional.ofNullable(locationApiResponseDto.getState()).orElse("Unknown"))
                            .latitude(locationApiResponseDto.getLat())
                            .longitude(locationApiResponseDto.getLon())
                            .build()
            );

            return ResponseEntity.noContent().build();
        }
        catch (DataIntegrityViolationException e) {
            checkForDuplicateConstraint(e);

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorDto("", "The location '" + locationApiResponseDto.getName() + "' already exists", "DUPLICATED_ERROR"));
        }
    }

    @DeleteMapping("/location/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable("id") Long locationId) {
        try {
            locationService.deleteById(locationId);
            return ResponseEntity.noContent().build();
        }
        catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiErrorDto("", "Access denied", "FORBIDDEN"));
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorDto("", "Failed to delete location", "DATA_ACCESS_ERROR"));
        }
    }

    @GetMapping("locations")
    public ResponseEntity<?> getLocations(@RequestParam Integer pageNumber, Principal principal) {
        Slice<LocationReadDto> locations = locationService.findAllByUserLogin(principal.getName(), Objects.requireNonNullElse(pageNumber, 0));

        return ResponseEntity.ok(locations);
    }

    private void checkForDuplicateConstraint(DataIntegrityViolationException e) {
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
        String constraintName = constraintViolationException.getConstraintName();
        if (!"idx_base_target".equals(constraintName)) {
            throw e;
        }
    }
}
