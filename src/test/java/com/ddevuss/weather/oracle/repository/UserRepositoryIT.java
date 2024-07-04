package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.annotation.IntegrationTest;
import com.ddevuss.weather.oracle.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@RequiredArgsConstructor
class UserRepositoryIT {

    private final UserRepository userRepository;

    @Test
    void findByLogin() {
        var user = userRepository.findByLogin("user1");

        assertThat(user.isPresent())
                .as("User with login 'user1' should be present")
                .isTrue();
    }

    @Test
    void findByLoginAndPassword() {
        var user = userRepository.findByLoginAndPassword("user1", "password1");

        assertThat(user.isPresent())
                .as("User with login 'user1' and password 'password1' should be present")
                .isTrue();
    }

    @Test
    void saveUser() {
        var user = userRepository.save(User.builder()
                .login("Test")
                .password("test")
                .build());

        assertThat(user).as("Must return notnull saved user.").isNotNull();
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void deleteUser() {
        var result = userRepository.deleteByLoginAndPassword("user1", "password1");

        assertThat(result).as("Must return int greater than 0.").isGreaterThan(0);
        assertThat(userRepository.findByLogin("user1")).isEmpty();
    }

    @Test
    void updateUsersLogin() {
        String newLogin = "newLogin";
        var result = userRepository.updateLoginByLoginAndPassword("user1",
                "password1",
                newLogin);

        assertThat(result).as("Must return int greater than 0.").isGreaterThan(0);
        assertThat(userRepository.findByLogin("user1")).isEmpty();
        assertThat(userRepository.findByLogin(newLogin)).isPresent();
    }

    @Test
    void updateUsersPassword() {
        String newPassword = "newPassword";
        var result = userRepository.updatePasswordByLoginAndPassword("user1",
                "password1",
                newPassword);

        assertThat(result).as("Must return int greater than 0.").isGreaterThan(0);
        assertThat(userRepository.findByLoginAndPassword("user1", "password1")).isEmpty();
        assertThat(userRepository.findByLoginAndPassword("user1", newPassword)).isPresent();
    }

}