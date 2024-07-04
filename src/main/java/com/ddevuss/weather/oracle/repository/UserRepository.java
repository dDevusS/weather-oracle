package com.ddevuss.weather.oracle.repository;

import com.ddevuss.weather.oracle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndPassword(String login, String password);

    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM users " +
                    "WHERE login = :login " +
                    "AND password = :password")
    int deleteByLoginAndPassword(@Param("login") String login, @Param("password") String password);

    @Modifying
    @Query(nativeQuery = true,
    value = "UPDATE users " +
            "SET login = :newLogin " +
            "WHERE login = :login " +
            "AND password = :password")
    int updateLoginByLoginAndPassword(@Param("login") String login,
                                                 @Param("password") String password,
                                                 @Param("newLogin") String newLogin);
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE users " +
                    "SET password = :newPassword " +
                    "WHERE login = :login " +
                    "AND password = :password")
    int updatePasswordByLoginAndPassword(@Param("login") String login,
                                         @Param("password") String password,
                                         @Param("newPassword") String newPassword);
}
