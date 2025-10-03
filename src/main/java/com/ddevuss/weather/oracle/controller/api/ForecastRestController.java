package com.ddevuss.weather.oracle.controller.api;

import com.ddevuss.weather.oracle.controller.api.docs.ForecastController;
import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationDto;
import com.ddevuss.weather.oracle.service.IOpenWeatherService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/forecast")
public class ForecastRestController implements ForecastController {

    private final IOpenWeatherService openWeatherService;

    @PostMapping
    public ResponseEntity<List<ForecastDto>> get(@RequestBody List<@Valid @NotNull LocationDto> locations) {
        if (locations.isEmpty()) return ResponseEntity.ok(List.of());
        return ResponseEntity.ok(openWeatherService.getWeatherForecast(locations));
    }
}
