package com.ddevuss.weather.oracle.auth.domain.internal;

import com.ddevuss.weather.oracle.auth.domain.User;
import com.ddevuss.weather.oracle.auth.dto.UserCreateDto;
import com.ddevuss.weather.oracle.common.mapper.DtoToEntityMapper;
import com.ddevuss.weather.oracle.common.mapper.EntityToDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
class UserMapper implements DtoToEntityMapper<UserCreateDto, User>, EntityToDtoMapper<User, UserCreateDto> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User dtoToEntity(UserCreateDto dto) {
        return User.builder()
                .login(dto.getLogin())
                .password(passwordEncoder.encode(dto.getRawPassword()))
                .build();
    }

    @Override
    public UserCreateDto entityToDto(User entity) {
        return UserCreateDto.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .build();
    }
}
