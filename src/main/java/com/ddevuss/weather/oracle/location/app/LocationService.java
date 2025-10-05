package com.ddevuss.weather.oracle.location.app;

import com.ddevuss.weather.oracle.location.domain.LocationRepository;
import com.ddevuss.weather.oracle.location.domain.LocationDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationDto> searchLocationByName(String locationName) {
        return locationRepository.searchLocationByName(locationName);
    }

    public LocationDto save(LocationDto locationDto, String username) {
        return locationRepository.save(locationDto, username);
    }

    public void deleteById(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    public Slice<LocationDto> findAllByUserLogin(String login, Integer pageNumber) {
        return locationRepository.findAllByUserLogin(login, pageNumber);
    }
}
