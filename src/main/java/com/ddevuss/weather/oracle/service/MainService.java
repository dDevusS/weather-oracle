package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.dto.UserInfoDto;
import com.ddevuss.weather.oracle.dto.UserReadDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MainService {

    private final UserService userService;
    private final LocationService locationService;
    private final OpenWeatherService openWeatherService;

    public UserInfoDto getUserInfoByLogin(String login) {
        UserReadDto user = userService.findByLogin(login);
        List<LocationReadDto> locations = locationService.findAllByUserId(user.getId());
        List<ForecastDto> forecasts = openWeatherService.getWeatherForecast(locations);

        return UserInfoDto.builder()
                .user(user)
                .forecasts(forecasts)
                .build();
    }

}
