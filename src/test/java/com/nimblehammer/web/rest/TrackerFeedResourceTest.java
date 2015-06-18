package com.nimblehammer.web.rest;

import com.nimblehammer.domain.ProjectFeed;
import com.nimblehammer.domain.TrackerActivity;
import com.nimblehammer.domain.TrackerFeed;
import com.nimblehammer.domain.TrackerPerformer;
import com.nimblehammer.domain.util.CalendarEventFactory;
import com.nimblehammer.repository.TrackerFeedRepository;
import com.nimblehammer.service.TrackerService;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrackerFeedResourceTest {
    @Mock
    private TrackerFeedRepository trackerFeedRepository;

    @Mock
    private TrackerService trackerService;

    @Mock
    private CalendarEventFactory calendarEventFactory;

    @InjectMocks
    private TrackerFeedResource trackerFeedResource;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trackerFeedResource).build();
    }

    @Test
    public void createTrackerFeed() throws Exception {
        mockMvc.perform(post("/api/trackerFeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content("{\"projectId\":\"123456\"}"))
            .andExpect(status().isCreated());

        ArgumentCaptor<TrackerFeed> argument = ArgumentCaptor.forClass(TrackerFeed.class);
        verify(trackerFeedRepository).save(argument.capture());
        MatcherAssert.assertThat(argument.getValue().getProjectId(), equalTo("123456"));
    }

    @Test
    public void checkProjectIdIsRequired() throws Exception {
        mockMvc.perform(post("/api/trackerFeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content("{\"projectId\":null}"))
            .andExpect(status().isBadRequest());

        verifyZeroInteractions(trackerFeedRepository);
    }

    @Test
    public void getAllTrackerFeeds() throws Exception {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setId(2L);
        projectFeed.setTitle("Project Title");

        TrackerFeed trackerFeed = new TrackerFeed();
        trackerFeed.setId(1L);
        trackerFeed.setProjectId("987654");
        trackerFeed.setProjectFeed(projectFeed);

        List<TrackerFeed> allTrackerFeeds = new ArrayList<>();
        allTrackerFeeds.add(trackerFeed);

        when(trackerFeedRepository.findAll()).thenReturn(allTrackerFeeds);

        mockMvc.perform(get("/api/trackerFeeds"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string("[{" +
                "\"id\":1," +
                "\"projectId\":\"987654\"," +
                "\"projectFeed\":{" +
                "\"id\":2," +
                "\"title\":\"Project Title\"," +
                "\"owner\":null," +
                "\"trackerFeeds\":null" +
                "}" +
                "}]"));
    }

    @Test
    public void getOneTrackerFeed() throws Exception {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setId(2L);
        projectFeed.setTitle("Project Title");

        TrackerFeed trackerFeed = new TrackerFeed();
        trackerFeed.setId(1L);
        trackerFeed.setProjectId("987654");
        trackerFeed.setProjectFeed(projectFeed);

        when(trackerFeedRepository.findOne(1L)).thenReturn(trackerFeed);

        mockMvc.perform(get("/api/trackerFeeds/{id}", trackerFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string("{" +
                "\"id\":1," +
                "\"projectId\":\"987654\"," +
                "\"projectFeed\":{" +
                "\"id\":2," +
                "\"title\":\"Project Title\"," +
                "\"owner\":null," +
                "\"trackerFeeds\":null" +
                "}" +
                "}"));
    }

    @Test
    public void getNonExistingTrackerFeed() throws Exception {
        when(trackerFeedRepository.findOne(Long.MAX_VALUE)).thenReturn(null);
        mockMvc.perform(get("/api/trackerFeeds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTrackerFeed() throws Exception {
        mockMvc.perform(put("/api/trackerFeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content("{\"id\":1,\"projectId\":\"654321\"}"))
            .andExpect(status().isOk());

        ArgumentCaptor<TrackerFeed> argument = ArgumentCaptor.forClass(TrackerFeed.class);
        verify(trackerFeedRepository).save(argument.capture());
        MatcherAssert.assertThat(argument.getValue().getProjectId(), equalTo("654321"));
    }

    @Test
    public void deleteTrackerFeed() throws Exception {
        mockMvc.perform(delete("/api/trackerFeeds/100"))
            .andExpect(status().isOk());
        verify(trackerFeedRepository).delete(100L);
    }

    @Test
    public void getTrackerFeedEvents() throws Exception {
        TrackerFeed trackerFeed = new TrackerFeed();
        trackerFeed.setId(1234L);
        trackerFeed.setProjectId("999");

        when(trackerFeedRepository.findOne(1234L)).thenReturn(trackerFeed);

        TrackerActivity trackerActivity = new TrackerActivity();
        trackerActivity.setKind("test kind");
        trackerActivity.setMessage("test message");
        trackerActivity.setOccurred_at(Instant.parse("2015-06-12T15:56:15Z"));

        TrackerPerformer trackerPerformer = new TrackerPerformer();
        trackerPerformer.setInitials("TA");

        trackerActivity.setPerformed_by(trackerPerformer);

        List<TrackerActivity> trackerActivities = new ArrayList<>();
        trackerActivities.add(trackerActivity);

        when(trackerService.getProjectActivities("999", "2094335")).thenReturn(trackerActivities);

        when(calendarEventFactory.create(trackerActivity)).thenCallRealMethod();

        mockMvc.perform(get("/api/trackerFeeds/1234/events")
            .header("X-TrackerToken", "2094335")
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().string("[{\"title\":\"TA test kind\"," +
                "\"start\":\"2015-06-12T15:56:15\"," +
                "\"end\":\"2015-06-12T17:56:15\"," +
                "\"allDay\":false" +
                "}]"));
    }
}
