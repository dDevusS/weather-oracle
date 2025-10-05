package com.ddevuss.weather.oracle.forecast.app;

import com.ddevuss.weather.oracle.forecast.domain.Forecast;
import com.ddevuss.weather.oracle.forecast.domain.ForecastRepository;
import com.ddevuss.weather.oracle.location.dto.LocationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ForecastService {

    private final ForecastRepository forecastRepository;

    public List<Forecast> getForecasts(List<LocationDto> locations) {
        return forecastRepository.getForecasts(locations);
    }
}
