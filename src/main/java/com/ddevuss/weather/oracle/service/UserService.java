package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.UserCreateDto;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.exception.LoginNotUniqueException;
import com.ddevuss.weather.oracle.mapper.UserCreateDtoToEntityMapper;
import com.ddevuss.weather.oracle.mapper.UserReadDtoFromEntityMapper;
import com.ddevuss.weather.oracle.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserReadDtoFromEntityMapper userReadDtoFromEntityMapper;
    private final UserCreateDtoToEntityMapper userCreateDtoToEntityMapper;

    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(()
                        -> new BadCredentialsException("User with login \"" + login + "\" not found or password was wrong."));

        return org.springframework.security.core.userdetails.User.withUsername(user.getLogin())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    @Transactional
    public void save(UserCreateDto userCreateDto) {
        if (userRepository.findByLogin(userCreateDto.getLogin()).isPresent()) {
            throw new LoginNotUniqueException();
        }
        User user = userCreateDtoToEntityMapper.dtoToEntity(userCreateDto);

        userReadDtoFromEntityMapper.entityToDto(userRepository.saveAndFlush(user));
    }

}