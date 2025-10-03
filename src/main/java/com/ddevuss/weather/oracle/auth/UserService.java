package com.ddevuss.weather.oracle.auth;

import com.ddevuss.weather.oracle.common.utils.UniqueConstraintTranslator;
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
public class UserService implements UserDetailsService {

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
            User user = userMapper.dtoToEntity(userDto);
            User savedUser = userRepository.saveAndFlush(user);
            return userMapper.entityToDto(savedUser);
        }
        catch (DataIntegrityViolationException e) {
            UniqueConstraintTranslator.checkConstraint(e, USER_LOGIN_UNQ)
                    .withMessage("The user with login '" + userDto.getLogin() + "' already exists")
                    .throwIfMatches();
            throw e;
        }
    }

}