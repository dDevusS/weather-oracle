package com.ddevuss.weather.oracle.mapper;

import com.ddevuss.weather.oracle.dto.UserCreateDto;
import com.ddevuss.weather.oracle.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserCreateDtoToEntityMapper implements DtoToEntityMapper<UserCreateDto, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User dtoToEntity(UserCreateDto dto) {
        return User.builder()
                .login(dto.getLogin())
                .password(passwordEncoder.encode(dto.getRawPassword()))
                .build();
    }
}
