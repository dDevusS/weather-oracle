package com.ddevuss.weather.oracle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationResponseDto implements Serializable {

    String name;

    Double lat;

    Double lon;

    String country;

    String state;
}
