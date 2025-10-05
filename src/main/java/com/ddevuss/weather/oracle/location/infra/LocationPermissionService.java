package com.ddevuss.weather.oracle.location.infra;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import static com.ddevuss.weather.oracle.common.Constants.CORRELATION_ID;
import static com.ddevuss.weather.oracle.common.Constants.USERNAME;

@Slf4j
@AllArgsConstructor
@Service
class LocationPermissionService {

    private static final String USER_DOESNT_HAVE_PERMISSION_TEMPLATE = "User '{}' doesn't have permission to delete location {}. correlationId={}";
    private static final String ACCESS_DENIED_EXCEPTION_MESSAGE_TEMPLATE = "User '%s' doesn't have permission to delete location %d";
    private static final String LOCATION_NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE = "Location with id %d not found";


    private final LocationJpaRepository locationJpaRepository;

    public boolean hasPermissionToDeleteLocation(Long locationId) {
        String currentUsername = MDC.get(USERNAME);

        return locationJpaRepository.findById(locationId)
                .map(location -> {
                    if (!location.getUser().getLogin().equals(currentUsername)) {
                        log.atWarn()
                                .addArgument(currentUsername)
                                .addArgument(locationId)
                                .addArgument(MDC.get(CORRELATION_ID))
                                .log(USER_DOESNT_HAVE_PERMISSION_TEMPLATE);

                        throw new AccessDeniedException(
                                String.format(ACCESS_DENIED_EXCEPTION_MESSAGE_TEMPLATE,
                                        currentUsername, locationId)
                        );
                    }
                    return true;
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(LOCATION_NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE, locationId)
                ));
    }

}
