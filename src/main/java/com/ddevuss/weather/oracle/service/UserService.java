package com.ddevuss.weather.oracle.service;

import com.ddevuss.weather.oracle.dto.UserDto;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.mapper.UserMapper;
import com.ddevuss.weather.oracle.repository.UserRepository;
import com.ddevuss.weather.oracle.utils.DuplicateConstraintChecker;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService implements UserDetailsService {

    private static final String USER_LOGIN_KEY_CONSTRAINT = "users_login_key";
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(()
                        -> new BadCredentialsException("User with login \"" + login + "\" not found or password was wrong."));

        return org.springframework.security.core.userdetails.User.withUsername(user.getLogin())
                .password(user.getPassword())
                .build();
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        try {
            return ((Function<UserDto, User>) userMapper::dtoToEntity)
                    .andThen(userRepository::saveAndFlush)
                    .andThen(userMapper::entityToDto)
                    .apply(userDto);
        }
        catch (DataIntegrityViolationException e) {
            if (DuplicateConstraintChecker.isThisConstraint(e, USER_LOGIN_KEY_CONSTRAINT)) {
                throw new DataIntegrityViolationException("The user with login:  " + userDto.getLogin() + " already exists", e);
            }
            else {
                throw e;
            }
        }
    }

}