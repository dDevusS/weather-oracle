package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SecurityService {

    private final LocationRepository locationRepository;

    public boolean hasPermissionToDeleteLocation(Long locationId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return locationRepository.findById(locationId)
                .map(location -> {
                    if (!location.getUser().getLogin().equals(currentUsername)) {
                        throw new AccessDeniedException(
                                String.format("User '%s' doesn't have permission to delete location %d",
                                        currentUsername, locationId)
                        );
                    }
                    return true;
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Location with id %d not found", locationId)
                ));
    }

}
