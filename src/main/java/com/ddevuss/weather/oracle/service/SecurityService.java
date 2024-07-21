package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SecurityService {

    private final LocationRepository locationRepository;

    public boolean hasPermissionToDeleteLocation(Long locationId) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return locationRepository.findById(locationId)
                .map(location -> location.getUser().getLogin().equals(login))
                .orElseThrow();
    }

}
