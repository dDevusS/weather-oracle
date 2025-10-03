package com.ddevuss.weather.oracle.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto implements Serializable {

    Long id;

    @NotBlank
    String name;

    String country;

    String state;

    @NotNull
    Double lat;

    @NotNull
    Double lon;

}
