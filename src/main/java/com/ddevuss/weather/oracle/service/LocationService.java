package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.mapper.LocationReadDtoFromEntityMapper;
import com.ddevuss.weather.oracle.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationReadDtoFromEntityMapper locationReadDtoFromEntityMapper;

    public List<LocationReadDto> findAllByUserId(Integer userId) {
        return locationRepository.findAllByUserId(userId).stream()
                .map(locationReadDtoFromEntityMapper::entityToDto)
                .toList();
    }

    @Transactional
    public LocationReadDto save(Location location) {
        return locationReadDtoFromEntityMapper.entityToDto(locationRepository.save(location));
    }
}
