package com.ddevuss.weather.oracle.forecast.web.internal;

import com.ddevuss.weather.oracle.forecast.app.ForecastService;
import com.ddevuss.weather.oracle.forecast.domain.Forecast;
import com.ddevuss.weather.oracle.forecast.web.doc.ForecastRestController;
import com.ddevuss.weather.oracle.location.dto.LocationDto;
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
class ForecastRestControllerImp implements ForecastRestController {

    private final ForecastService forecastService;

    @PostMapping
    public ResponseEntity<List<Forecast>> get(@RequestBody List<@Valid @NotNull LocationDto> locations) {
        if (locations.isEmpty()) return ResponseEntity.ok(List.of());
        return ResponseEntity.ok(forecastService.getForecasts(locations));
    }
}
