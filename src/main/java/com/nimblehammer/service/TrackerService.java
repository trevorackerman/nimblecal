package com.nimblehammer.service;

import com.nimblehammer.client.RestClient;
import com.nimblehammer.domain.TrackerActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TrackerService {
    private final Logger logger = LoggerFactory.getLogger(TrackerService.class);
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

        logger.warn("fetching tracker activities");

        long before = new Date().getTime();
        TrackerActivity[] activities = restClient.get(TRACKER_URL + "/" + projectId + "/activity", TrackerActivity[].class);
        long after = new Date().getTime();
        long elapsed = after - before;
        logger.warn("fetched " + activities.length + " activities from tracker " + elapsed + " elapsed");
        trackerActivities.addAll(Arrays.asList(activities));

        return trackerActivities;
    }
}
