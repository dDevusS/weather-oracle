package com.ddevuss.weather.oracle.auth;

import com.ddevuss.weather.oracle.common.mapper.DtoToEntityMapper;
import com.ddevuss.weather.oracle.common.mapper.EntityToDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper implements DtoToEntityMapper<UserDto, User>, EntityToDtoMapper<User, UserDto> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User dtoToEntity(UserDto dto) {
        return User.builder()
                .login(dto.getLogin())
                .password(passwordEncoder.encode(dto.getRawPassword()))
                .build();
    }

    @Override
    public UserDto entityToDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .build();
    }
}
