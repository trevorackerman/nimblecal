package com.nimblehammer.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nimblehammer.domain.util.ISO8601LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class CalendarEvent {

    private final String title;
    @JsonSerialize(using = ISO8601LocalDateTimeSerializer.class)
    private final LocalDateTime start;
    @JsonSerialize(using = ISO8601LocalDateTimeSerializer.class)
    private final LocalDateTime end;
    private final boolean allDay = false;

    private CalendarEvent(String title, LocalDateTime start) {
        this.title = title;
        this.start = start;
        this.end = start.plusHours(2);
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public static class Builder {
        private String title;
        private LocalDateTime start;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder start(LocalDateTime start) {
            this.start = start;
            return this;
        }

        public CalendarEvent build() {
            return new CalendarEvent(title, start);
        }
    }
}
