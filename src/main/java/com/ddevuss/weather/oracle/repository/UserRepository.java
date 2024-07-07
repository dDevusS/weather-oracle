package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndPassword(String login, String password);

    @Modifying
    int deleteByLoginAndPassword(String login,
                                 String password);

    @Modifying
    @Query(value = "UPDATE User u " +
                   "SET u.login = :newLogin " +
                   "WHERE u.login = :login " +
                   "AND u.password = :password")
    int updateLoginByLoginAndPassword(String login,
                                      String password,
                                      String newLogin);

    @Modifying
    @Query(value = "UPDATE User u " +
                   "SET u.password = :newPassword " +
                   "WHERE u.login = :login " +
                   "AND u.password = :password")
    int updatePasswordByLoginAndPassword(String login,
                                         String password,
                                         String newPassword);

}
