package com.ddevuss.weather.oracle.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationApiResponseDto implements Serializable {

    String name;

    Double lat;

    Double lon;

    String country;

    String state;
}
