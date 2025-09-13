package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.LocationDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.mapper.LocationMapper;
import com.ddevuss.weather.oracle.repository.LocationRepository;
import com.ddevuss.weather.oracle.utils.DuplicateConstraintChecker;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    private static final String LOCATION_KEY_CONSTRAINT = "idx_base_target";
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private static final Integer PAGE_SIZE = 4;

    public Slice<LocationDto> findAllByUserLogin(String login, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("id"));
        return locationRepository.findAllByUserLogin(login, pageRequest)
                .map(locationMapper::entityToDto);
    }

    @Transactional
    public void save(LocationDto locationDto, String userLogin) {
        try {
            Location location = locationMapper.dtoToEntity(locationDto);
            location.setUser(User.builder()
                    .login(userLogin)
                    .build());

            locationRepository.saveLocation(location);
        }
        catch (DataIntegrityViolationException e) {
            if (DuplicateConstraintChecker.isThisConstraint(e, LOCATION_KEY_CONSTRAINT)) {
                throw new DataIntegrityViolationException("The location '" + locationDto.getName() + "' already exists", e);
            }
            else {
                throw e;
            }
        }
    }

    @Transactional
    @PreAuthorize("@securityService.hasPermissionToDeleteLocation(#locationId)")
    public void deleteById(Long locationId) {
        locationRepository.deleteById(locationId);
    }

}
