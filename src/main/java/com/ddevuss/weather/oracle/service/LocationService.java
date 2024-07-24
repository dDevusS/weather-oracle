package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.LocationReadDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.exception.NotUniqueLocationException;
import com.ddevuss.weather.oracle.mapper.LocationReadDtoFromEntityMapper;
import com.ddevuss.weather.oracle.repository.LocationRepository;
import com.ddevuss.weather.oracle.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final LocationReadDtoFromEntityMapper locationReadDtoFromEntityMapper;

    public Slice<LocationReadDto> findAllByUserLogin(String login, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 4, Sort.by("id"));
        return locationRepository.findAllByUserLogin(login, pageRequest)
                .map(locationReadDtoFromEntityMapper::entityToDto);
    }

    @Transactional
    public void save(Location location) {
        User user = userRepository.findByLogin(location.getUser().getLogin()).orElseThrow();
        location.setUser(user);
        ensureUniqueLocationForUser(location);
        locationReadDtoFromEntityMapper.entityToDto(locationRepository.save(location));
    }

    @Transactional
    @PreAuthorize("@securityService.hasPermissionToDeleteLocation(#locationId)")
    public void deleteById(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    private void ensureUniqueLocationForUser(Location location) {
        locationRepository.findByUserIdAndLocationLatitudeAndLocationLongitude(location.getUser().getId(),
                location.getLatitude(),
                location.getLongitude()).ifPresent(existingLocation -> {
            throw new NotUniqueLocationException();
        });
    }
}
