package com.ddevuss.weather.oracle.auth.domain.user.internal;

import com.ddevuss.weather.oracle.auth.domain.user.User;
import com.ddevuss.weather.oracle.auth.domain.user.UserRepository;
import com.ddevuss.weather.oracle.auth.domain.user.UserService;
import com.ddevuss.weather.oracle.auth.dto.UserCreateDto;
import com.ddevuss.weather.oracle.auth.mapper.UserMapper;
import com.ddevuss.weather.oracle.common.util.UniqueConstraintTranslator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ddevuss.weather.oracle.common.entity.ConstraintKey.USER_LOGIN_UNQ;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
class UserServiceImp implements UserDetailsService, UserService {

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
    public UserCreateDto save(UserCreateDto userCreateDto) {
        try {
            User user = userMapper.toEntity(userCreateDto);
            User savedUser = userRepository.saveAndFlush(user);
            return userMapper.toDto(savedUser);
        }
        catch (DataIntegrityViolationException e) {
            UniqueConstraintTranslator.checkConstraint(e, USER_LOGIN_UNQ)
                    .withMessage("The user with login '" + userCreateDto.getLogin() + "' already exists")
                    .throwIfMatches();
            throw e;
        }
    }

}