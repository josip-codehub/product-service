package com.ingemark.demo.products.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

    public static long calculateTimeToMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().atStartOfDay().plusDays(1);
        return ChronoUnit.SECONDS.between(now, midnight);
    }
}
