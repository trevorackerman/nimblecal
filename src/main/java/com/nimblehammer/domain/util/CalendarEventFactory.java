package com.nimblehammer.domain.util;

import com.nimblehammer.domain.CalendarEvent;
import com.nimblehammer.domain.TrackerActivity;
import com.nimblehammer.domain.TrackerResource;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class CalendarEventFactory {
    private final Map<String, String> trackerActivityKindMap = new HashMap<>();

    public CalendarEventFactory() {
        trackerActivityKindMap.put("comment_create_activity", "commented");
        trackerActivityKindMap.put("comment_update_activity", "commented");
        trackerActivityKindMap.put("story_move_activity", "moved");
        trackerActivityKindMap.put("story_create_activity", "created");
    }

    public CalendarEvent create(TrackerActivity trackerActivity) {
        TrackerResource trackerResource = trackerActivity.getPrimary_resources()[0];
        String title = trackerActivityKindMap.get(trackerActivity.getKind());
        if (title == null) {
            title = trackerActivity.getKind();
        }

        if ("story_update_activity".equals(title)) {
            title = trackerActivity.getMessage();
            String[] tokens = title.split(" this ")[0].split(" ");
            title = tokens[tokens.length - 1];
        }

        title += " " + trackerResource.getId();

        String description = "<div class=\"tiny-padding\">" +
            "<span>" + trackerActivity.getOccurred_at().toString() + "</span>" +
            "<span class=\"tiny-padding-left\"><a href=\"" + trackerResource.getUrl() + "\">" + trackerResource.getId() + "</a></span>" +
            "</div>" +
            "<div class=\"tiny-padding\">" + trackerResource.getName() + "</div>";
        String message = "<p>" + trackerActivity.getMessage() + "</p>";

        return new CalendarEvent.Builder()
            .start(LocalDateTime.ofInstant(trackerActivity.getOccurred_at(), ZoneOffset.UTC))
            .title(title)
            .description(description)
            .message(message)
            .avatarAlternate(trackerActivity.getPerformed_by().getInitials())
            .build();
    }
}
