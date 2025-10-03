package com.ddevuss.weather.oracle.location.domain;

import com.ddevuss.weather.oracle.location.dto.LocationDto;
import org.springframework.data.domain.Slice;

public interface LocationService {

    Slice<LocationDto> findAllByUserLogin(String login, Integer pageNumber);

    LocationDto save(LocationDto locationDto, String userLogin);

    void deleteById(Long locationId);
}
