package com.ddevuss.weather.oracle.mapper;

import com.ddevuss.weather.oracle.dto.UserReadDto;
import com.ddevuss.weather.oracle.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadDtoMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User object) {
        return new UserReadDto(object.getId(), object.getLogin());
    }
}
