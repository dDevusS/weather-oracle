package com.ddevuss.weather.oracle.controller.api;

import com.ddevuss.weather.oracle.controller.api.docs.ForecastController;
import com.ddevuss.weather.oracle.dto.ApiErrorDto;
import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.service.OpenWeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/forecast")
public class ForecastRestController implements ForecastController {

    private final OpenWeatherService openWeatherService;

    @PostMapping
    public ResponseEntity<?> getWeatherForecast(@RequestBody List<LocationReadDto> locations) {
        return Optional.ofNullable(locations)
                .map(list -> {
                    if (list.isEmpty()) return ResponseEntity.ok(List.of());
                    List<ForecastDto> forecasts;

                    try {
                        forecasts = openWeatherService.getWeatherForecast(list);
                        return ResponseEntity.ok(forecasts);
                    }
                    catch (RestClientResponseException ex) {
                        return ResponseEntity.internalServerError().body(new ApiErrorDto("INTERNAL_ERROR", "Something went wrong", "INTERNAL"));
                    }
                })
                .orElse(ResponseEntity.badRequest().build());
    }
}
