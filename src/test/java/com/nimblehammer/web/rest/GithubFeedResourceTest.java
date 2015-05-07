package com.nimblehammer.web.rest;

import com.nimblehammer.Application;
import com.nimblehammer.domain.GithubFeed;
import com.nimblehammer.repository.GithubFeedRepository;

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
 * Test class for the GithubFeedResource REST controller.
 *
 * @see GithubFeedResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GithubFeedResourceTest {

    private static final String DEFAULT_REPOSITORY_URL = "SAMPLE_TEXT";
    private static final String UPDATED_REPOSITORY_URL = "UPDATED_TEXT";

    @Inject
    private GithubFeedRepository githubFeedRepository;

    private MockMvc restGithubFeedMockMvc;

    private GithubFeed githubFeed;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GithubFeedResource githubFeedResource = new GithubFeedResource();
        ReflectionTestUtils.setField(githubFeedResource, "githubFeedRepository", githubFeedRepository);
        this.restGithubFeedMockMvc = MockMvcBuilders.standaloneSetup(githubFeedResource).build();
    }

    @Before
    public void initTest() {
        githubFeed = new GithubFeed();
        githubFeed.setRepositoryURL(DEFAULT_REPOSITORY_URL);
    }

    @Test
    @Transactional
    public void createGithubFeed() throws Exception {
        int databaseSizeBeforeCreate = githubFeedRepository.findAll().size();

        // Create the GithubFeed
        restGithubFeedMockMvc.perform(post("/api/githubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(githubFeed)))
                .andExpect(status().isCreated());

        // Validate the GithubFeed in the database
        List<GithubFeed> githubFeeds = githubFeedRepository.findAll();
        assertThat(githubFeeds).hasSize(databaseSizeBeforeCreate + 1);
        GithubFeed testGithubFeed = githubFeeds.get(githubFeeds.size() - 1);
        assertThat(testGithubFeed.getRepositoryURL()).isEqualTo(DEFAULT_REPOSITORY_URL);
    }

    @Test
    @Transactional
    public void checkRepositoryURLIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(githubFeedRepository.findAll()).hasSize(0);
        // set the field null
        githubFeed.setRepositoryURL(null);

        // Create the GithubFeed, which fails.
        restGithubFeedMockMvc.perform(post("/api/githubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(githubFeed)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<GithubFeed> githubFeeds = githubFeedRepository.findAll();
        assertThat(githubFeeds).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllGithubFeeds() throws Exception {
        // Initialize the database
        githubFeedRepository.saveAndFlush(githubFeed);

        // Get all the githubFeeds
        restGithubFeedMockMvc.perform(get("/api/githubFeeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(githubFeed.getId().intValue())))
                .andExpect(jsonPath("$.[*].repositoryURL").value(hasItem(DEFAULT_REPOSITORY_URL.toString())));
    }

    @Test
    @Transactional
    public void getGithubFeed() throws Exception {
        // Initialize the database
        githubFeedRepository.saveAndFlush(githubFeed);

        // Get the githubFeed
        restGithubFeedMockMvc.perform(get("/api/githubFeeds/{id}", githubFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(githubFeed.getId().intValue()))
            .andExpect(jsonPath("$.repositoryURL").value(DEFAULT_REPOSITORY_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGithubFeed() throws Exception {
        // Get the githubFeed
        restGithubFeedMockMvc.perform(get("/api/githubFeeds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGithubFeed() throws Exception {
        // Initialize the database
        githubFeedRepository.saveAndFlush(githubFeed);

		int databaseSizeBeforeUpdate = githubFeedRepository.findAll().size();

        // Update the githubFeed
        githubFeed.setRepositoryURL(UPDATED_REPOSITORY_URL);
        restGithubFeedMockMvc.perform(put("/api/githubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(githubFeed)))
                .andExpect(status().isOk());

        // Validate the GithubFeed in the database
        List<GithubFeed> githubFeeds = githubFeedRepository.findAll();
        assertThat(githubFeeds).hasSize(databaseSizeBeforeUpdate);
        GithubFeed testGithubFeed = githubFeeds.get(githubFeeds.size() - 1);
        assertThat(testGithubFeed.getRepositoryURL()).isEqualTo(UPDATED_REPOSITORY_URL);
    }

    @Test
    @Transactional
    public void deleteGithubFeed() throws Exception {
        // Initialize the database
        githubFeedRepository.saveAndFlush(githubFeed);

		int databaseSizeBeforeDelete = githubFeedRepository.findAll().size();

        // Get the githubFeed
        restGithubFeedMockMvc.perform(delete("/api/githubFeeds/{id}", githubFeed.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<GithubFeed> githubFeeds = githubFeedRepository.findAll();
        assertThat(githubFeeds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
