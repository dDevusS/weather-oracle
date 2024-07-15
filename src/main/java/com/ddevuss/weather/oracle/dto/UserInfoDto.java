package com.ddevuss.weather.oracle.dto;

import com.ddevuss.weather.oracle.entity.Location;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserInfoDto {

    Integer id;

    String login;

    List<Location> locations;

}
