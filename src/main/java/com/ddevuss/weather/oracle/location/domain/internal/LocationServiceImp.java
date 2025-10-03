package com.ddevuss.weather.oracle.location.domain.internal;

import com.ddevuss.weather.oracle.auth.domain.User;
import com.ddevuss.weather.oracle.auth.domain.UserRepository;
import com.ddevuss.weather.oracle.common.utils.UniqueConstraintTranslator;
import com.ddevuss.weather.oracle.location.domain.Location;
import com.ddevuss.weather.oracle.location.domain.LocationRepository;
import com.ddevuss.weather.oracle.location.domain.LocationService;
import com.ddevuss.weather.oracle.location.dto.LocationDto;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ddevuss.weather.oracle.common.entity.ConstraintKey.LOCATION_COORDINATE_UNIQUE;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
class LocationServiceImp implements LocationService {

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
    public LocationDto save(LocationDto locationDto, String userLogin) {
        try {
            Location location = locationMapper.dtoToEntity(locationDto);
            User user = userRepository.findByLogin(userLogin).orElseThrow();
            location.setUser(user);
            return locationMapper.entityToDto(locationRepository.save(location));
        }
        catch (DataIntegrityViolationException e) {
            UniqueConstraintTranslator.checkConstraint(e, LOCATION_COORDINATE_UNIQUE)
                    .withMessage("The location '" + locationDto.getName() + "' already exists")
                    .throwIfMatches();
            throw e;
        }
    }

    @Transactional
    @PreAuthorize("@locationPermissionService.hasPermissionToDeleteLocation(#locationId)")
    public void deleteById(Long locationId) {
        locationRepository.deleteById(locationId);
    }

}
