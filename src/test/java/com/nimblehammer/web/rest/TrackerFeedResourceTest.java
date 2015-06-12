package com.nimblehammer.web.rest;

import com.nimblehammer.Application;
import com.nimblehammer.domain.TrackerFeed;
import com.nimblehammer.repository.TrackerFeedRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TrackerFeedResource REST controller.
 *
 * @see TrackerFeedResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TrackerFeedResourceTest {

    private static final String DEFAULT_PROJECT_ID = "SAMPLE_TEXT";
    private static final String UPDATED_PROJECT_ID = "UPDATED_TEXT";

    @Inject
    private TrackerFeedRepository trackerFeedRepository;

    private MockMvc restTrackerFeedMockMvc;

    private TrackerFeed trackerFeed;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrackerFeedResource trackerFeedResource = new TrackerFeedResource();
        ReflectionTestUtils.setField(trackerFeedResource, "trackerFeedRepository", trackerFeedRepository);
        this.restTrackerFeedMockMvc = MockMvcBuilders.standaloneSetup(trackerFeedResource).build();
    }

    @Before
    public void initTest() {
        trackerFeed = new TrackerFeed();
        trackerFeed.setProjectId(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void createTrackerFeed() throws Exception {
        int databaseSizeBeforeCreate = trackerFeedRepository.findAll().size();

        // Create the TrackerFeed
        restTrackerFeedMockMvc.perform(post("/api/trackerFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trackerFeed)))
                .andExpect(status().isCreated());

        // Validate the TrackerFeed in the database
        List<TrackerFeed> trackerFeeds = trackerFeedRepository.findAll();
        assertThat(trackerFeeds).hasSize(databaseSizeBeforeCreate + 1);
        TrackerFeed testTrackerFeed = trackerFeeds.get(trackerFeeds.size() - 1);
        assertThat(testTrackerFeed.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void checkProjectIdIsRequired() throws Exception {
        int sizeBefore = trackerFeedRepository.findAll().size();
        // set the field null
        trackerFeed.setProjectId(null);

        // Create the TrackerFeed, which fails.
        restTrackerFeedMockMvc.perform(post("/api/trackerFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trackerFeed)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<TrackerFeed> trackerFeeds = trackerFeedRepository.findAll();
        assertThat(trackerFeeds.size()).isEqualTo(sizeBefore);
    }

    @Test
    @Transactional
    public void getAllTrackerFeeds() throws Exception {
        // Initialize the database
        trackerFeedRepository.saveAndFlush(trackerFeed);

        // Get all the trackerFeeds
        restTrackerFeedMockMvc.perform(get("/api/trackerFeeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trackerFeed.getId().intValue())))
                .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.toString())));
    }

    @Test
    @Transactional
    public void getTrackerFeed() throws Exception {
        // Initialize the database
        trackerFeedRepository.saveAndFlush(trackerFeed);

        // Get the trackerFeed
        restTrackerFeedMockMvc.perform(get("/api/trackerFeeds/{id}", trackerFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(trackerFeed.getId().intValue()))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTrackerFeed() throws Exception {
        // Get the trackerFeed
        restTrackerFeedMockMvc.perform(get("/api/trackerFeeds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrackerFeed() throws Exception {
        // Initialize the database
        trackerFeedRepository.saveAndFlush(trackerFeed);

		int databaseSizeBeforeUpdate = trackerFeedRepository.findAll().size();

        // Update the trackerFeed
        trackerFeed.setProjectId(UPDATED_PROJECT_ID);
        restTrackerFeedMockMvc.perform(put("/api/trackerFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trackerFeed)))
                .andExpect(status().isOk());

        // Validate the TrackerFeed in the database
        List<TrackerFeed> trackerFeeds = trackerFeedRepository.findAll();
        assertThat(trackerFeeds).hasSize(databaseSizeBeforeUpdate);
        TrackerFeed testTrackerFeed = trackerFeeds.get(trackerFeeds.size() - 1);
        assertThat(testTrackerFeed.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    public void deleteTrackerFeed() throws Exception {
        // Initialize the database
        trackerFeedRepository.saveAndFlush(trackerFeed);

		int databaseSizeBeforeDelete = trackerFeedRepository.findAll().size();

        // Get the trackerFeed
        restTrackerFeedMockMvc.perform(delete("/api/trackerFeeds/{id}", trackerFeed.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TrackerFeed> trackerFeeds = trackerFeedRepository.findAll();
        assertThat(trackerFeeds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
