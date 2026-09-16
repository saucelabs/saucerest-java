package com.saucelabs.saucerest.model.insights;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

final class DateTimeUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private DateTimeUtils() {
    }

    /** Converts a {@link LocalDateTime} in the system default time zone to a UTC API date string. */
    static String toUtcString(LocalDateTime val) {
        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(val);
        LocalDateTime utcDateTime = val.minusSeconds(offset.getTotalSeconds());

        return utcDateTime.format(FORMATTER);
    }
}
