package com.ddevuss.weather.oracle.location.domain;

import org.springframework.data.domain.Slice;

import java.util.List;

public interface LocationRepository {

    Slice<LocationDto> findAllByUserLogin(String login, Integer pageNumber);

    LocationDto save(LocationDto locationDto, String userLogin);

    void deleteById(Long locationId);

    List<LocationDto> searchLocationByName(String locationName);
}
