package com.ddevuss.weather.oracle.mapper;

import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationReadDtoFromEntityMapper implements EntityToDtoMapper<LocationReadDto, Location> {

    @Override
    public LocationReadDto entityToDto(Location entity) {
        return LocationReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .state(entity.getState())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }
}
