package com.nimblehammer.domain;

import java.time.Instant;

public class CalendarEvent {
    private final String title;
    private final Instant start;
    private final boolean allDay = false;

    private CalendarEvent(String title, Instant start) {
        this.title = title;
        this.start = start;
    }

    public static class Builder {
        private String title;
        private Instant start;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder start(Instant start) {
            this.start = start;
            return this;
        }

        public CalendarEvent build() {
            return new CalendarEvent(title, start);
        }
    }
}
