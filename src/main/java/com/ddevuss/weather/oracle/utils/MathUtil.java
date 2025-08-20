package com.ddevuss.weather.oracle.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {

    public static double truncateCoordinate(double value, int precision) {
        return BigDecimal.valueOf(value)
                .setScale(precision, RoundingMode.FLOOR)
                .doubleValue();
    }
}
