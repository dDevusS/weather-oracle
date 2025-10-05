package com.ddevuss.weather.oracle.location.infra.client.logging;

import com.ddevuss.weather.oracle.location.infra.client.LocationSearchClient;
import com.ddevuss.weather.oracle.location.domain.LocationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Primary
@Service
class LocationSearchLoggingDecorator implements LocationSearchClient {

    private static final String MESSAGE_SUCCESS_TEMPLATE = "Request to Open Weather API for {} method has been processed successfully";
    private static final String MESSAGE_GEO_REQUEST_TEMPLATE = "Sending request to Open Weather API for searching location with name {}";

    private static final String METHOD_NAME_SEARCH_LOCATION_BY_NAME = "searchLocationByName";

    private final LocationSearchClient target;

    public LocationSearchLoggingDecorator(@Qualifier("locationSearchClientImp") LocationSearchClient target) {
        this.target = target;
    }

    @Override
    public List<LocationDto> searchLocationByName(String locationName) {
        log.debug(MESSAGE_GEO_REQUEST_TEMPLATE, locationName);
        var locations = target.searchLocationByName(locationName);
        log.debug(MESSAGE_SUCCESS_TEMPLATE, METHOD_NAME_SEARCH_LOCATION_BY_NAME);
        return locations;

    }
}
