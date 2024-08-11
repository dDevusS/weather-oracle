package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.mapper.LocationReadDtoFromEntityMapper;
import com.ddevuss.weather.oracle.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationReadDtoFromEntityMapper locationReadDtoFromEntityMapper;
    private static final Integer PAGE_SIZE = 4;

    public Slice<LocationReadDto> findAllByUserLogin(String login, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("id"));
        return locationRepository.findAllByUserLogin(login, pageRequest)
                .map(locationReadDtoFromEntityMapper::entityToDto);
    }

    @Transactional
    public void save(Location location) {
        locationRepository.saveLocation(location);
    }

    @Transactional
    @PreAuthorize("@securityService.hasPermissionToDeleteLocation(#locationId)")
    public void deleteById(Long locationId) {
        locationRepository.deleteById(locationId);
    }

}
