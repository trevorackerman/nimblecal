package com.nimblehammer.web.rest;

import com.nimblehammer.Application;
import com.nimblehammer.domain.JiraFeed;
import com.nimblehammer.repository.JiraFeedRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JiraFeedResource REST controller.
 *
 * @see JiraFeedResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JiraFeedResourceTest {

    private static final String DEFAULT_SITE = "SAMPLE_TEXT";
    private static final String UPDATED_SITE = "UPDATED_TEXT";
    private static final String DEFAULT_PROJECT_ID = "SAMPLE_TEXT";
    private static final String UPDATED_PROJECT_ID = "UPDATED_TEXT";

    @Inject
    private JiraFeedRepository jiraFeedRepository;

    private MockMvc restJiraFeedMockMvc;

    private JiraFeed jiraFeed;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JiraFeedResource jiraFeedResource = new JiraFeedResource();
        ReflectionTestUtils.setField(jiraFeedResource, "jiraFeedRepository", jiraFeedRepository);
        this.restJiraFeedMockMvc = MockMvcBuilders.standaloneSetup(jiraFeedResource).build();
    }

    @Before
    public void initTest() {
        jiraFeed = new JiraFeed();
        jiraFeed.setSite(DEFAULT_SITE);
        jiraFeed.setProjectId(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void createJiraFeed() throws Exception {
        int databaseSizeBeforeCreate = jiraFeedRepository.findAll().size();

        // Create the JiraFeed
        restJiraFeedMockMvc.perform(post("/api/jiraFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jiraFeed)))
                .andExpect(status().isCreated());

        // Validate the JiraFeed in the database
        List<JiraFeed> jiraFeeds = jiraFeedRepository.findAll();
        assertThat(jiraFeeds).hasSize(databaseSizeBeforeCreate + 1);
        JiraFeed testJiraFeed = jiraFeeds.get(jiraFeeds.size() - 1);
        assertThat(testJiraFeed.getSite()).isEqualTo(DEFAULT_SITE);
        assertThat(testJiraFeed.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void checkSiteIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(jiraFeedRepository.findAll()).hasSize(0);
        // set the field null
        jiraFeed.setSite(null);

        // Create the JiraFeed, which fails.
        restJiraFeedMockMvc.perform(post("/api/jiraFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jiraFeed)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<JiraFeed> jiraFeeds = jiraFeedRepository.findAll();
        assertThat(jiraFeeds).hasSize(0);
    }

    @Test
    @Transactional
    public void checkProjectIdIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(jiraFeedRepository.findAll()).hasSize(0);
        // set the field null
        jiraFeed.setProjectId(null);

        // Create the JiraFeed, which fails.
        restJiraFeedMockMvc.perform(post("/api/jiraFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jiraFeed)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<JiraFeed> jiraFeeds = jiraFeedRepository.findAll();
        assertThat(jiraFeeds).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllJiraFeeds() throws Exception {
        // Initialize the database
        jiraFeedRepository.saveAndFlush(jiraFeed);

        // Get all the jiraFeeds
        restJiraFeedMockMvc.perform(get("/api/jiraFeeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jiraFeed.getId().intValue())))
                .andExpect(jsonPath("$.[*].site").value(hasItem(DEFAULT_SITE.toString())))
                .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.toString())));
    }

    @Test
    @Transactional
    public void getJiraFeed() throws Exception {
        // Initialize the database
        jiraFeedRepository.saveAndFlush(jiraFeed);

        // Get the jiraFeed
        restJiraFeedMockMvc.perform(get("/api/jiraFeeds/{id}", jiraFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jiraFeed.getId().intValue()))
            .andExpect(jsonPath("$.site").value(DEFAULT_SITE.toString()))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJiraFeed() throws Exception {
        // Get the jiraFeed
        restJiraFeedMockMvc.perform(get("/api/jiraFeeds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJiraFeed() throws Exception {
        // Initialize the database
        jiraFeedRepository.saveAndFlush(jiraFeed);

		int databaseSizeBeforeUpdate = jiraFeedRepository.findAll().size();

        // Update the jiraFeed
        jiraFeed.setSite(UPDATED_SITE);
        jiraFeed.setProjectId(UPDATED_PROJECT_ID);
        restJiraFeedMockMvc.perform(put("/api/jiraFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jiraFeed)))
                .andExpect(status().isOk());

        // Validate the JiraFeed in the database
        List<JiraFeed> jiraFeeds = jiraFeedRepository.findAll();
        assertThat(jiraFeeds).hasSize(databaseSizeBeforeUpdate);
        JiraFeed testJiraFeed = jiraFeeds.get(jiraFeeds.size() - 1);
        assertThat(testJiraFeed.getSite()).isEqualTo(UPDATED_SITE);
        assertThat(testJiraFeed.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    public void deleteJiraFeed() throws Exception {
        // Initialize the database
        jiraFeedRepository.saveAndFlush(jiraFeed);

		int databaseSizeBeforeDelete = jiraFeedRepository.findAll().size();

        // Get the jiraFeed
        restJiraFeedMockMvc.perform(delete("/api/jiraFeeds/{id}", jiraFeed.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<JiraFeed> jiraFeeds = jiraFeedRepository.findAll();
        assertThat(jiraFeeds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
