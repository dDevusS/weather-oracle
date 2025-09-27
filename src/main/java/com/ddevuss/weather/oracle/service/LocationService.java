package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.LocationDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.mapper.LocationMapper;
import com.ddevuss.weather.oracle.repository.LocationRepository;
import com.ddevuss.weather.oracle.repository.UserRepository;
import com.ddevuss.weather.oracle.utils.UniqueConstraintTranslator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ddevuss.weather.oracle.entity.ConstraintKey.LOCATION_COORDINATE_UNIQUE;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final LocationMapper locationMapper;
    private static final Integer PAGE_SIZE = 4;

    public Slice<LocationDto> findAllByUserLogin(String login, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("id"));
        return locationRepository.findAllByUserLogin(login, pageRequest)
                .map(locationMapper::entityToDto);
    }

    @Transactional
    public Location save(LocationDto locationDto, String userLogin) {
        try {
            Location location = locationMapper.dtoToEntity(locationDto);
            User user = userRepository.findByLogin(userLogin).orElseThrow();
            location.setUser(user);
            return locationRepository.save(location);
        }
        catch (DataIntegrityViolationException e) {
            UniqueConstraintTranslator.checkConstraint(e, LOCATION_COORDINATE_UNIQUE)
                    .withMessage("The location '" + locationDto.getName() + "' already exists")
                    .throwIfMatches();
            throw e;
        }
    }

    @Transactional
    @PreAuthorize("@securityService.hasPermissionToDeleteLocation(#locationId)")
    public void deleteById(Long locationId) {
        locationRepository.deleteById(locationId);
    }

}
