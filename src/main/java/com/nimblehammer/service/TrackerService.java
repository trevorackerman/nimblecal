package com.nimblehammer.service;

import com.nimblehammer.client.RestClient;
import com.nimblehammer.domain.TrackerActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        restClient.setHeader("X-TrackerToken", apiToken);
        restClient.setParameter("occurred_after", dateTimeService.getISO8601BeginningOfMonth());
        ResponseEntity responseEntity = restClient.get(TRACKER_URL + "/" + projectId, TrackerActivity[].class);

        List<TrackerActivity> trackerActivities = new ArrayList<>();

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            trackerActivities.addAll(Arrays.asList((TrackerActivity[]) responseEntity.getBody()));
        }

        return trackerActivities;
    }
}
