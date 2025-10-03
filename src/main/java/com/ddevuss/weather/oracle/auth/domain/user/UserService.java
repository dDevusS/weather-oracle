package com.ddevuss.weather.oracle.auth.domain.user;

import com.ddevuss.weather.oracle.auth.dto.UserCreateDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails loadUserByUsername(String login);

    UserCreateDto save(UserCreateDto userCreateDto);
}
