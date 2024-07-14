package com.ddevuss.weather.oracle.dto;

import com.ddevuss.weather.oracle.entity.Location;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class UserEnvironmentDto {

    Integer id;

    String login;

    List<Location> locations;

}
