package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.repository.LocationRepository;
import com.ddevuss.weather.oracle.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SecurityService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public boolean hasPermissionToDeleteLocation(Long locationId) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return locationRepository.findById(locationId)
                .map(location -> location.getUser().getLogin().equals(login))
                .orElseThrow();
    }

    public boolean hasPermissionToSaveLocation(Location location) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return login.equals(location.getUser().getLogin());
    }
}
