package com.nimblehammer.web.rest;

import com.nimblehammer.Application;
import com.nimblehammer.domain.BitbucketFeed;
import com.nimblehammer.repository.BitbucketFeedRepository;

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
 * Test class for the BitbucketFeedResource REST controller.
 *
 * @see BitbucketFeedResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BitbucketFeedResourceTest {

    private static final String DEFAULT_SITE = "SAMPLE_TEXT";
    private static final String UPDATED_SITE = "UPDATED_TEXT";
    private static final String DEFAULT_REPOSITORY_OWNER = "SAMPLE_TEXT";
    private static final String UPDATED_REPOSITORY_OWNER = "UPDATED_TEXT";
    private static final String DEFAULT_REPOSITORY_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_REPOSITORY_NAME = "UPDATED_TEXT";

    @Inject
    private BitbucketFeedRepository bitbucketFeedRepository;

    private MockMvc restBitbucketFeedMockMvc;

    private BitbucketFeed bitbucketFeed;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BitbucketFeedResource bitbucketFeedResource = new BitbucketFeedResource();
        ReflectionTestUtils.setField(bitbucketFeedResource, "bitbucketFeedRepository", bitbucketFeedRepository);
        this.restBitbucketFeedMockMvc = MockMvcBuilders.standaloneSetup(bitbucketFeedResource).build();
    }

    @Before
    public void initTest() {
        bitbucketFeed = new BitbucketFeed();
        bitbucketFeed.setSite(DEFAULT_SITE);
        bitbucketFeed.setRepositoryOwner(DEFAULT_REPOSITORY_OWNER);
        bitbucketFeed.setRepositoryName(DEFAULT_REPOSITORY_NAME);
    }

    @Test
    @Transactional
    public void createBitbucketFeed() throws Exception {
        int databaseSizeBeforeCreate = bitbucketFeedRepository.findAll().size();

        // Create the BitbucketFeed
        restBitbucketFeedMockMvc.perform(post("/api/bitbucketFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bitbucketFeed)))
                .andExpect(status().isCreated());

        // Validate the BitbucketFeed in the database
        List<BitbucketFeed> bitbucketFeeds = bitbucketFeedRepository.findAll();
        assertThat(bitbucketFeeds).hasSize(databaseSizeBeforeCreate + 1);
        BitbucketFeed testBitbucketFeed = bitbucketFeeds.get(bitbucketFeeds.size() - 1);
        assertThat(testBitbucketFeed.getSite()).isEqualTo(DEFAULT_SITE);
        assertThat(testBitbucketFeed.getRepositoryOwner()).isEqualTo(DEFAULT_REPOSITORY_OWNER);
        assertThat(testBitbucketFeed.getRepositoryName()).isEqualTo(DEFAULT_REPOSITORY_NAME);
    }

    @Test
    @Transactional
    public void checkSiteIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(bitbucketFeedRepository.findAll()).hasSize(0);
        // set the field null
        bitbucketFeed.setSite(null);

        // Create the BitbucketFeed, which fails.
        restBitbucketFeedMockMvc.perform(post("/api/bitbucketFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bitbucketFeed)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<BitbucketFeed> bitbucketFeeds = bitbucketFeedRepository.findAll();
        assertThat(bitbucketFeeds).hasSize(0);
    }

    @Test
    @Transactional
    public void checkRepositoryOwnerIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(bitbucketFeedRepository.findAll()).hasSize(0);
        // set the field null
        bitbucketFeed.setRepositoryOwner(null);

        // Create the BitbucketFeed, which fails.
        restBitbucketFeedMockMvc.perform(post("/api/bitbucketFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bitbucketFeed)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<BitbucketFeed> bitbucketFeeds = bitbucketFeedRepository.findAll();
        assertThat(bitbucketFeeds).hasSize(0);
    }

    @Test
    @Transactional
    public void checkRepositoryNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(bitbucketFeedRepository.findAll()).hasSize(0);
        // set the field null
        bitbucketFeed.setRepositoryName(null);

        // Create the BitbucketFeed, which fails.
        restBitbucketFeedMockMvc.perform(post("/api/bitbucketFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bitbucketFeed)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<BitbucketFeed> bitbucketFeeds = bitbucketFeedRepository.findAll();
        assertThat(bitbucketFeeds).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllBitbucketFeeds() throws Exception {
        // Initialize the database
        bitbucketFeedRepository.saveAndFlush(bitbucketFeed);

        // Get all the bitbucketFeeds
        restBitbucketFeedMockMvc.perform(get("/api/bitbucketFeeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bitbucketFeed.getId().intValue())))
                .andExpect(jsonPath("$.[*].site").value(hasItem(DEFAULT_SITE.toString())))
                .andExpect(jsonPath("$.[*].repositoryOwner").value(hasItem(DEFAULT_REPOSITORY_OWNER.toString())))
                .andExpect(jsonPath("$.[*].repositoryName").value(hasItem(DEFAULT_REPOSITORY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBitbucketFeed() throws Exception {
        // Initialize the database
        bitbucketFeedRepository.saveAndFlush(bitbucketFeed);

        // Get the bitbucketFeed
        restBitbucketFeedMockMvc.perform(get("/api/bitbucketFeeds/{id}", bitbucketFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bitbucketFeed.getId().intValue()))
            .andExpect(jsonPath("$.site").value(DEFAULT_SITE.toString()))
            .andExpect(jsonPath("$.repositoryOwner").value(DEFAULT_REPOSITORY_OWNER.toString()))
            .andExpect(jsonPath("$.repositoryName").value(DEFAULT_REPOSITORY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBitbucketFeed() throws Exception {
        // Get the bitbucketFeed
        restBitbucketFeedMockMvc.perform(get("/api/bitbucketFeeds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBitbucketFeed() throws Exception {
        // Initialize the database
        bitbucketFeedRepository.saveAndFlush(bitbucketFeed);

		int databaseSizeBeforeUpdate = bitbucketFeedRepository.findAll().size();

        // Update the bitbucketFeed
        bitbucketFeed.setSite(UPDATED_SITE);
        bitbucketFeed.setRepositoryOwner(UPDATED_REPOSITORY_OWNER);
        bitbucketFeed.setRepositoryName(UPDATED_REPOSITORY_NAME);
        restBitbucketFeedMockMvc.perform(put("/api/bitbucketFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bitbucketFeed)))
                .andExpect(status().isOk());

        // Validate the BitbucketFeed in the database
        List<BitbucketFeed> bitbucketFeeds = bitbucketFeedRepository.findAll();
        assertThat(bitbucketFeeds).hasSize(databaseSizeBeforeUpdate);
        BitbucketFeed testBitbucketFeed = bitbucketFeeds.get(bitbucketFeeds.size() - 1);
        assertThat(testBitbucketFeed.getSite()).isEqualTo(UPDATED_SITE);
        assertThat(testBitbucketFeed.getRepositoryOwner()).isEqualTo(UPDATED_REPOSITORY_OWNER);
        assertThat(testBitbucketFeed.getRepositoryName()).isEqualTo(UPDATED_REPOSITORY_NAME);
    }

    @Test
    @Transactional
    public void deleteBitbucketFeed() throws Exception {
        // Initialize the database
        bitbucketFeedRepository.saveAndFlush(bitbucketFeed);

		int databaseSizeBeforeDelete = bitbucketFeedRepository.findAll().size();

        // Get the bitbucketFeed
        restBitbucketFeedMockMvc.perform(delete("/api/bitbucketFeeds/{id}", bitbucketFeed.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BitbucketFeed> bitbucketFeeds = bitbucketFeedRepository.findAll();
        assertThat(bitbucketFeeds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
