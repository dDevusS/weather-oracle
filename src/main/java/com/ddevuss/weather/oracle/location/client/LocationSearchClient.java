package com.ddevuss.weather.oracle.location.client;

import com.ddevuss.weather.oracle.location.dto.LocationDto;

import java.util.List;

public interface LocationSearchClient {

    List<LocationDto> searchLocationByName(String locationName);
}
