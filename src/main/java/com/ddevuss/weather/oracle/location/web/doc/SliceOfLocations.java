package com.ddevuss.weather.oracle.location.web.doc;

import com.ddevuss.weather.oracle.location.domain.LocationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SliceOfLocations {

    private List<LocationDto> content;

    private int number;

    private int size;

    private boolean first;

    private boolean last;

    private boolean empty;

}
