package com.ddevuss.weather.oracle.auth.mapper;

import com.ddevuss.weather.oracle.auth.domain.User;
import com.ddevuss.weather.oracle.auth.dto.UserCreateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public abstract class UserMapper {

    protected PasswordEncoder passwordEncoder;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "login")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.getRawPassword()))")
    public abstract User toEntity(UserCreateDto dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "login")
    public abstract UserCreateDto toDto(User entity);

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
