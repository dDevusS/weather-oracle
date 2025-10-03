package com.ddevuss.weather.oracle.auth;

import com.ddevuss.weather.oracle.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users", indexes = @Index(name = "ind_user_login", columnList = "login"))
public class User extends BaseEntity {

    @Column(name = "login", unique = true, nullable = false)
    @NotBlank
    private String login;

    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;

}
