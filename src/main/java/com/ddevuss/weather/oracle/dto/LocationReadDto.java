package com.ddevuss.weather.oracle.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class LocationReadDto implements Serializable {

    Long id;

    String name;

    String state;

    Double latitude;

    Double longitude;
}
