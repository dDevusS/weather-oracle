package com.ddevuss.weather.oracle.controller.api.docs;

import com.ddevuss.weather.oracle.dto.LocationReadDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SliceOfLocations {

    private List<LocationReadDto> content;

    private int number;

    private int size;

    private boolean hasNext;

}
