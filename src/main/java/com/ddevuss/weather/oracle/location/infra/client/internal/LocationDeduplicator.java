package com.ddevuss.weather.oracle.location.infra.client.internal;

import com.ddevuss.weather.oracle.location.domain.LocationDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

class LocationDeduplicator {

    private static final int TRUNCATE_SCALE = 2;

    public static List<LocationDto> deduplicate(LocationDto[] locations) {
        return Arrays.stream(locations)
                .filter(Objects::nonNull)
                .filter(distinctBy(l -> Map.entry(
                        truncateCoordinate(l.getLat()),
                        truncateCoordinate(l.getLon())
                )))
                .filter(distinctBy(l -> List.of(
                        Optional.ofNullable(l.getName()).orElse(""),
                        Optional.ofNullable(l.getState()).orElse(""),
                        Optional.ofNullable(l.getCountry()).orElse("")
                )))
                .toList();
    }

    private static double truncateCoordinate(double value) {
        return BigDecimal.valueOf(value)
                .setScale(TRUNCATE_SCALE, RoundingMode.FLOOR)
                .doubleValue();
    }

    private static <T, K> Predicate<T> distinctBy(Function<? super T, K> keyExtractor) {
        Set<K> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
