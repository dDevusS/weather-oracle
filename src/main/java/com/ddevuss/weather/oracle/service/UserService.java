package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.UserEnvironmentDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.mapper.UserReadDtoMapper;
import com.ddevuss.weather.oracle.repository.LocationRepository;
import com.ddevuss.weather.oracle.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final SessionService sessionService;
    private final UserReadDtoMapper userReadDtoMapper;

    //TODO: realize request to another service instead repository

    public UserEnvironmentDto getUserEnvironmentById(Integer id) {
        var user = userRepository.findById(id)
                .map(userReadDtoMapper::map)
                .orElseThrow();
        UUID sessionId = sessionService.getSessionUuidByUserId(user.getId());
        List<Location> locations = locationRepository.findAllByUserId(user.getId());

        return new UserEnvironmentDto(user.getId(),
                user.getLogin(),
                locations,
                sessionId);
    }
}