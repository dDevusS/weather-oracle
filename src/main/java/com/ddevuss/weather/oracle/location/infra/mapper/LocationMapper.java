package com.ddevuss.weather.oracle.location.infra.mapper;

import com.ddevuss.weather.oracle.location.domain.Location;
import com.ddevuss.weather.oracle.location.domain.LocationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface LocationMapper {

    @Mapping(source = "lat", target = "latitude")
    @Mapping(source = "lon", target = "longitude")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "state", defaultValue = "Unknown")
    Location toEntity(LocationDto dto);

    @Mapping(source = "latitude", target = "lat")
    @Mapping(source = "longitude", target = "lon")
    @Mapping(target = "country", ignore = true)
    LocationDto toDto(Location entity);
}
