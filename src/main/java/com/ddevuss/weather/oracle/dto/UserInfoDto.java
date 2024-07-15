package com.ddevuss.weather.oracle.dto;

import com.ddevuss.weather.oracle.entity.Location;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@Builder
public class UserInfoDto implements Serializable {

    Integer id;

    String login;

    List<Location> locations;

}
