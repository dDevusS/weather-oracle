package com.ddevuss.weather.oracle.mapper;

import com.ddevuss.weather.oracle.dto.UserReadDto;
import com.ddevuss.weather.oracle.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadDtoMapper implements Mapper<UserReadDto, User> {

    @Override
    public UserReadDto entityToDto(User entity) {
        return UserReadDto.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .build();
    }
}
