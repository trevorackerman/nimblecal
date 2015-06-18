package com.nimblehammer.service;

import com.nimblehammer.client.RestClient;
import com.nimblehammer.domain.TrackerActivity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrackerService {
    public static final String TRACKER_URL = "https://www.pivotaltracker.com/services/v5/projects";

    @Autowired
    private RestClient restClient;

    @Autowired
    private DateTimeService dateTimeService;

    public List<TrackerActivity> getProjectActivities(String projectId, String apiToken) {
        List<TrackerActivity> trackerActivities = new ArrayList<>();

        if (apiToken != null && !apiToken.isEmpty()) {
            restClient.setHeader("X-TrackerToken", apiToken);
        }
        restClient.setParameter("occurred_after", dateTimeService.getISO8601BeginningOfMonth());

        TrackerActivity[] activities = restClient.get(TRACKER_URL + "/" + projectId + "/activity", TrackerActivity[].class);
        trackerActivities.addAll(Arrays.asList(activities));

        return trackerActivities;
    }
}
