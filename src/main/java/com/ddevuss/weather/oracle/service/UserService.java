package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.UserCreateDto;
import com.ddevuss.weather.oracle.dto.UserEnvironmentDto;
import com.ddevuss.weather.oracle.dto.UserReadDto;
import com.ddevuss.weather.oracle.entity.Location;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.exception.LoginNotUniqueException;
import com.ddevuss.weather.oracle.mapper.UserCreateDtoToEntityMapper;
import com.ddevuss.weather.oracle.mapper.UserReadDtoFromEntityMapper;
import com.ddevuss.weather.oracle.repository.LocationRepository;
import com.ddevuss.weather.oracle.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final UserReadDtoFromEntityMapper userReadDtoFromEntityMapper;
    private final UserCreateDtoToEntityMapper userCreateDtoToEntityMapper;

    //TODO: realize request to another service instead repository

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(()
                        -> new UsernameNotFoundException("User with login \"" + login + "\" not found."));

        return org.springframework.security.core.userdetails.User.withUsername(user.getLogin())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    public UserEnvironmentDto getUserEnvironmentById(Integer id) {
        var user = userRepository.findById(id)
                .map(userReadDtoFromEntityMapper::entityToDto)
                .orElseThrow();
        List<Location> locations = locationRepository.findAllByUserId(user.getId());
        return UserEnvironmentDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .locations(locations)
                .build();
    }

    @Transactional
    public UserReadDto save(UserCreateDto userCreateDto) {
        if (userRepository.findByLogin(userCreateDto.getLogin()).isPresent()) {
            throw new LoginNotUniqueException();
        }
        User user = userCreateDtoToEntityMapper.dtoToEntity(userCreateDto);
        return userReadDtoFromEntityMapper.entityToDto(userRepository.saveAndFlush(user));
    }
}