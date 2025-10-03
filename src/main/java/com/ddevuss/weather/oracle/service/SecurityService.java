package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class SecurityService {

    private final LocationRepository locationRepository;

    public boolean hasPermissionToDeleteLocation(Long locationId) {
        String currentUsername = MDC.get("username");

        return locationRepository.findById(locationId)
                .map(location -> {
                    if (!location.getUser().getLogin().equals(currentUsername)) {
                        log.atWarn()
                                .addArgument(currentUsername)
                                .addArgument(locationId)
                                .addArgument(MDC.get("correlationId"))
                                .log("User '{}' doesn't have permission to delete location {}. correlationId={}" );

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
