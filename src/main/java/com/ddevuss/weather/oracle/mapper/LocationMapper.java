package com.ddevuss.weather.oracle.mapper;

import com.ddevuss.weather.oracle.dto.LocationDto;
import com.ddevuss.weather.oracle.entity.Location;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LocationMapper implements DtoToEntityMapper<LocationDto, Location>, EntityToDtoMapper<Location, LocationDto> {

    @Override
    public Location dtoToEntity(LocationDto dto) {
        return Location.builder()
                .name(dto.getName())
                .state(Optional.ofNullable(dto.getState()).orElse("Unknown"))
                .latitude(dto.getLat())
                .longitude(dto.getLon())
                .build();
    }

    @Override
    public LocationDto entityToDto(Location entity) {
        return LocationDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .state(entity.getState())
                .lat(entity.getLatitude())
                .lon(entity.getLongitude())
                .build();
    }
}
