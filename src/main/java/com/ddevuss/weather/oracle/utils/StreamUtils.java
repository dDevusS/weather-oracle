package com.ddevuss.weather.oracle.utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class StreamUtils {

    public static <T, K> Predicate<T> distinctBy(Function<? super T, K> keyExtractor) {
        Set<K> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
