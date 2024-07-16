package com.ddevuss.weather.oracle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationResponseDto {

    String name;

    @JsonProperty("local_names")
    Map<String, String> localNames;

    Double lat;

    Double lon;

    String country;

    String state;
}
