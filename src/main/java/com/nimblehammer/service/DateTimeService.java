package com.nimblehammer.service;


import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeService {
    OffsetDateTime getBeginningOfMonth() {
        return OffsetDateTime.now().withDayOfMonth(1)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0);
    }

    String getISO8601BeginningOfMonth() {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(getBeginningOfMonth());
    }
}
