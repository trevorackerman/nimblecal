package com.nimblehammer.domain.util;

import com.nimblehammer.domain.CalendarEvent;
import com.nimblehammer.domain.TrackerActivity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CalendarEventFactory {
    public CalendarEvent create(TrackerActivity trackerActivity) {
        return new CalendarEvent.Builder()
            .start(LocalDateTime.ofInstant(trackerActivity.getOccurred_at(), ZoneOffset.UTC))
            .title(trackerActivity.getPerformed_by().getInitials() + " " + trackerActivity.getKind())
            .build();
    }
}
