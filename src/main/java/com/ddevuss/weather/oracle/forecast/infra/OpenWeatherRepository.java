package com.ddevuss.weather.oracle.forecast.infra;

import com.ddevuss.weather.oracle.forecast.domain.Forecast;
import com.ddevuss.weather.oracle.forecast.domain.ForecastRepository;
import com.ddevuss.weather.oracle.forecast.infra.client.ForecastClient;
import com.ddevuss.weather.oracle.location.domain.LocationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
class OpenWeatherRepository implements ForecastRepository {

    private final ForecastClient forecastClient;

    @Override
    public List<Forecast> getForecasts(List<LocationDto> locations) {
        return forecastClient.getWeatherForecast(locations);
    }
}
