package com.nimblehammer.web.rest;

import com.nimblehammer.Application;
import com.nimblehammer.domain.ProjectFeed;
import com.nimblehammer.repository.ProjectFeedRepository;
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
 * Test class for the ProjectFeedResource REST controller.
 *
 * @see ProjectFeedResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProjectFeedResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";

    @Inject
    private ProjectFeedRepository projectFeedRepository;

    private MockMvc restProjectFeedMockMvc;

    private ProjectFeed projectFeed;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectFeedResource projectFeedResource = new ProjectFeedResource();
        ReflectionTestUtils.setField(projectFeedResource, "projectFeedRepository", projectFeedRepository);
        this.restProjectFeedMockMvc = MockMvcBuilders.standaloneSetup(projectFeedResource).build();
    }

    @Before
    public void initTest() {
        projectFeed = new ProjectFeed();
        projectFeed.setTitle(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createProjectFeed() throws Exception {
        int databaseSizeBeforeCreate = projectFeedRepository.findAll().size();

        // Create the Project Feed
        restProjectFeedMockMvc.perform(post("/api/projectfeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectFeed)))
                .andExpect(status().isCreated());

        // Validate the Project Feed in the database
        List<ProjectFeed> projectFeeds = projectFeedRepository.findAll();
        assertThat(projectFeeds).hasSize(databaseSizeBeforeCreate + 1);
        ProjectFeed testProjectFeed = projectFeeds.get(projectFeeds.size() - 1);
        assertThat(testProjectFeed.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(projectFeedRepository.findAll()).hasSize(0);
        // set the field null
        projectFeed.setTitle(null);

        // Create the Project Feed, which fails.
        restProjectFeedMockMvc.perform(post("/api/projectfeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectFeed)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ProjectFeed> projectFeeds = projectFeedRepository.findAll();
        assertThat(projectFeeds).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllProjectFeeds() throws Exception {
        // Initialize the database
        projectFeedRepository.saveAndFlush(projectFeed);

        // Get all the projectFeeds
        restProjectFeedMockMvc.perform(get("/api/projectfeeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectFeed.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getProjectFeed() throws Exception {
        // Initialize the database
        projectFeedRepository.saveAndFlush(projectFeed);

        // Get the projectFeed
        restProjectFeedMockMvc.perform(get("/api/projectfeeds/{id}", projectFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(projectFeed.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectFeed() throws Exception {
        // Get the projectFeed
        restProjectFeedMockMvc.perform(get("/api/projectfeeds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectFeed() throws Exception {
        // Initialize the database
        projectFeedRepository.saveAndFlush(projectFeed);

		int databaseSizeBeforeUpdate = projectFeedRepository.findAll().size();

        // Update the projectFeed
        projectFeed.setTitle(UPDATED_TITLE);
        restProjectFeedMockMvc.perform(put("/api/projectfeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectFeed)))
                .andExpect(status().isOk());

        // Validate the Project Feed in the database
        List<ProjectFeed> projectFeeds = projectFeedRepository.findAll();
        assertThat(projectFeeds).hasSize(databaseSizeBeforeUpdate);
        ProjectFeed testProjectFeed = projectFeeds.get(projectFeeds.size() - 1);
        assertThat(testProjectFeed.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void deleteProjectFeed() throws Exception {
        // Initialize the database
        projectFeedRepository.saveAndFlush(projectFeed);

		int databaseSizeBeforeDelete = projectFeedRepository.findAll().size();

        // Get the projectFeed
        restProjectFeedMockMvc.perform(delete("/api/projectfeeds/{id}", projectFeed.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectFeed> projectFeeds = projectFeedRepository.findAll();
        assertThat(projectFeeds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
