package com.ingemark.demo.products.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

    private TimeUtil() {
    }

    public static long calculateTimeToMidnight() {
        return ChronoUnit.SECONDS.between(
                LocalDateTime.now(),
                LocalDate.now().atStartOfDay().plusDays(1)
        );
    }
}
