package com.nimblehammer.domain.util;

import com.nimblehammer.domain.CalendarEvent;
import com.nimblehammer.domain.TrackerActivity;

public class CalendarEventFactory {
    public CalendarEvent create(TrackerActivity trackerActivity) {
        return new CalendarEvent.Builder()
            .start(trackerActivity.getOccurred_at())
            .title(trackerActivity.getMessage())
            .build();
    }
}
