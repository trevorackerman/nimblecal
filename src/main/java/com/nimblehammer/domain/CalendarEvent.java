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
    private final String message;
    private final String description;
    private final String avatarUrl;
    private final String avatarAlternate;

    private CalendarEvent(String title, String description, String message, String avatarUrl, String avatarAlternate, LocalDateTime start) {
        this.title = title;
        this.start = start;
        this.end = start.plusSeconds(1);
        this.description = description;
        this.message = message;
        this.avatarUrl = avatarUrl;
        this.avatarAlternate = avatarAlternate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getAvatarAlternate() {
        return  avatarAlternate;
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
        private String description;
        private String message;
        private String avatarUrl;
        private String avatarAlternate;
        private LocalDateTime start;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder start(LocalDateTime start) {
            this.start = start;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder avatarAlternate(String avatarAlternate) {
            this.avatarAlternate = avatarAlternate;
            return this;
        }

        public CalendarEvent build() {
            return new CalendarEvent(title, description, message, avatarUrl, avatarAlternate, start);
        }
    }
}
