package com.nimblehammer.service;

import com.nimblehammer.client.RestClient;
import com.nimblehammer.domain.TrackerActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TrackerService.class, RestClient.class, DateTimeService.class})
public class TrackerServiceTest {
    @Mock
    RestClient restClient;

    @Mock
    DateTimeService dateTimeService;

    @InjectMocks
    TrackerService trackerService;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void itGetsProjectActivities() {
        OffsetDateTime after = OffsetDateTime.parse("2015-06-01T00:00:00-06:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        when(dateTimeService.getBeginningOfMonth()).thenReturn(after);
        when(dateTimeService.getISO8601BeginningOfMonth()).thenCallRealMethod();

        TrackerActivity[] trackerActivities = new TrackerActivity[2];

        TrackerActivity trackerActivity0 = new TrackerActivity();
        trackerActivity0.setMessage("Updated a story");

        TrackerActivity trackerActivity1 = new TrackerActivity();
        trackerActivity1.setMessage("Created a new story");

        trackerActivities[0] = trackerActivity0;
        trackerActivities[1] = trackerActivity1;

        when(restClient.get(TrackerService.TRACKER_URL + "/123456/activity", TrackerActivity[].class)).thenReturn(trackerActivities);

        List<TrackerActivity> fetchedProjectActivities = trackerService.getProjectActivities("123456", "f49jaeg4t949t4");
        verify(restClient).setHeader("X-TrackerToken", "f49jaeg4t949t4");
        verify(restClient).setParameter("occurred_after", "2015-06-01T00:00:00-06:00");

        assertThat(fetchedProjectActivities.size(), equalTo(2));
        assertThat(fetchedProjectActivities, containsInAnyOrder(trackerActivity0, trackerActivity1));
    }
}
