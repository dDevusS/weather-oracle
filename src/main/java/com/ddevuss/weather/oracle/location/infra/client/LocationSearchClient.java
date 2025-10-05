package com.ddevuss.weather.oracle.location.infra.client;

import com.ddevuss.weather.oracle.location.domain.LocationDto;

import java.util.List;

public interface LocationSearchClient {

    List<LocationDto> searchLocationByName(String locationName);
}
