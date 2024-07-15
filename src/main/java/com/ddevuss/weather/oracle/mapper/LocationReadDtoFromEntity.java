package com.ddevuss.weather.oracle.mapper;

import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.entity.Location;

public class LocationReadDtoFromEntity implements EntityToDtoMapper<LocationReadDto, Location> {

    @Override
    public LocationReadDto entityToDto(Location entity) {
        return LocationReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }
}
