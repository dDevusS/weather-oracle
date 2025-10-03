package com.ddevuss.weather.oracle.forecast.client.internal;

import com.ddevuss.weather.oracle.common.utils.MathUtil;
import com.ddevuss.weather.oracle.common.utils.StreamUtils;
import com.ddevuss.weather.oracle.location.dto.LocationDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

class LocationDeduplicator {

    private static final int TRUNCATE_SCALE = 2;

    public static List<LocationDto> deduplicate(LocationDto[] locations) {
        return Arrays.stream(locations)
                .filter(Objects::nonNull)
                .filter(StreamUtils.distinctBy(l -> Map.entry(
                        MathUtil.truncateCoordinate(l.getLat(), TRUNCATE_SCALE),
                        MathUtil.truncateCoordinate(l.getLon(), TRUNCATE_SCALE)
                )))
                .filter(StreamUtils.distinctBy(l -> List.of(
                        Optional.ofNullable(l.getName()).orElse(""),
                        Optional.ofNullable(l.getState()).orElse(""),
                        Optional.ofNullable(l.getCountry()).orElse("")
                )))
                .toList();
    }
}
