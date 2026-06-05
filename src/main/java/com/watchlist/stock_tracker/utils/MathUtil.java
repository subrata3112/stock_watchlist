package com.watchlist.stock_tracker.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class MathUtil {

    public long bigDecimalToLong(BigDecimal decimal) {
        if (decimal == null) return 0L;
        return decimal.setScale(2, RoundingMode.HALF_UP)
                .movePointRight(2)
                .longValue();
    }
}
