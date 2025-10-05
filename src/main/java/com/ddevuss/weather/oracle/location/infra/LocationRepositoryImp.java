package com.ddevuss.weather.oracle.location.infra;

import com.ddevuss.weather.oracle.auth.domain.user.User;
import com.ddevuss.weather.oracle.auth.domain.user.UserRepository;
import com.ddevuss.weather.oracle.common.util.UniqueConstraintTranslator;
import com.ddevuss.weather.oracle.location.domain.Location;
import com.ddevuss.weather.oracle.location.domain.LocationRepository;
import com.ddevuss.weather.oracle.location.domain.LocationDto;
import com.ddevuss.weather.oracle.location.infra.client.LocationSearchClient;
import com.ddevuss.weather.oracle.location.infra.mapper.LocationMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ddevuss.weather.oracle.common.entity.ConstraintKey.LOCATION_COORDINATE_UNIQUE;

@Transactional(readOnly = true)
@AllArgsConstructor
@Repository
public class LocationRepositoryImp implements LocationRepository {

    private final LocationSearchClient locationClient;
    private final UserRepository userRepository;
    private final LocationJpaRepository locationJpaRepository;
    private final LocationMapper locationMapper;
    private static final Integer PAGE_SIZE = 4;

    @Override
    public Slice<LocationDto> findAllByUserLogin(String login, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("id"));
        return locationJpaRepository.findAllByUserLogin(login, pageRequest)
                .map(locationMapper::toDto);
    }

    @Override
    @Transactional
    public LocationDto save(LocationDto locationDto, String userLogin) {
        try {
            Location location = locationMapper.toEntity(locationDto);
            User user = userRepository.findByLogin(userLogin).orElseThrow();
            location.setUser(user);
            return locationMapper.toDto(locationJpaRepository.save(location));
        }
        catch (DataIntegrityViolationException e) {
            UniqueConstraintTranslator.checkConstraint(e, LOCATION_COORDINATE_UNIQUE)
                    .withMessage("The location '" + locationDto.getName() + "' already exists")
                    .throwIfMatches();
            throw e;
        }
    }

    @Override
    @Transactional
    @PreAuthorize("@locationPermissionService.hasPermissionToDeleteLocation(#locationId)")
    public void deleteById(Long locationId) {
        locationJpaRepository.deleteById(locationId);
    }

    @Override
    public List<LocationDto> searchLocationByName(String locationName) {
        return locationClient.searchLocationByName(locationName);
    }
}
