package com.nimblehammer.web.rest;

import com.nimblehammer.domain.GithubFeed;
import com.nimblehammer.repository.GithubFeedRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GithubFeedResourceTest {

    private static final String UPDATED_REPOSITORY_URL = "UPDATED_TEXT";

    @Mock
    private GithubFeedRepository githubFeedRepository;

    private MockMvc restGithubFeedMockMvc;

    @InjectMocks
    private GithubFeedResource githubFeedResource;

    private GithubFeed existingGithubFeed;

    private List<GithubFeed> githubFeeds = new ArrayList<>();

    @Before
    public void before() {
        initMocks(this);
        this.restGithubFeedMockMvc = MockMvcBuilders.standaloneSetup(githubFeedResource).build();

        existingGithubFeed = new GithubFeed();
        existingGithubFeed.setRepositoryURL("https://www.example.com/johnwayne/projectx");
        existingGithubFeed.setRepositoryName("projectx");
        existingGithubFeed.setRepositoryOwner("johnwayne");
        existingGithubFeed.setId(100L);

        githubFeeds.add(existingGithubFeed);

        when(githubFeedRepository.findOne(100L)).thenReturn(existingGithubFeed);
        when(githubFeedRepository.findAll()).thenReturn(githubFeeds);
    }

    @Test
    public void createGithubFeed() throws Exception {
        GithubFeed githubFeed = new GithubFeed();
        githubFeed.setRepositoryName("javaproject");
        githubFeed.setRepositoryOwner("duke");
        githubFeed.setRepositoryURL("https://example.com/duke/javaproject");

        restGithubFeedMockMvc.perform(post("/api/githubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(githubFeed)))
                .andExpect(status().isCreated());

        verify(githubFeedRepository).save(githubFeed);
    }

    @Test
    public void checkRepositoryURLIsRequired() throws Exception {
        GithubFeed githubFeed = new GithubFeed();
        githubFeed.setRepositoryName("javaproject");
        githubFeed.setRepositoryOwner("duke");
        githubFeed.setRepositoryURL(null);

        restGithubFeedMockMvc.perform(post("/api/githubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(githubFeed)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(githubFeedRepository);
    }

    @Test
    public void checkRepositoryNameIsRequired() throws Exception {
        GithubFeed githubFeed = new GithubFeed();
        githubFeed.setRepositoryName(null);
        githubFeed.setRepositoryOwner("duke");
        githubFeed.setRepositoryURL("https://example.com/duke/javaproject");

        restGithubFeedMockMvc.perform(post("/api/githubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(githubFeed)))
            .andExpect(status().isBadRequest());

        verifyZeroInteractions(githubFeedRepository);
    }

    @Test
    public void checkRepositoryOwnerIsRequired() throws Exception {
        GithubFeed githubFeed = new GithubFeed();
        githubFeed.setRepositoryName("javaproject");
        githubFeed.setRepositoryOwner(null);
        githubFeed.setRepositoryURL("https://example.com/duke/javaproject");

        restGithubFeedMockMvc.perform(post("/api/githubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(githubFeed)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(githubFeedRepository);
    }

    @Test
    public void getAllGithubFeeds() throws Exception {
        restGithubFeedMockMvc.perform(get("/api/githubFeeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(existingGithubFeed.getId().intValue())))
                .andExpect(jsonPath("$.[*].repositoryURL").value(hasItem("https://www.example.com/johnwayne/projectx")));
        verify(githubFeedRepository).findAll();
    }

    @Test
    public void getGithubFeed() throws Exception {
        restGithubFeedMockMvc.perform(get("/api/githubFeeds/{id}", existingGithubFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(existingGithubFeed.getId().intValue()))
            .andExpect(jsonPath("$.repositoryURL").value("https://www.example.com/johnwayne/projectx"));

        verify(githubFeedRepository).findOne(existingGithubFeed.getId());
    }

    @Test
    public void getNonExistingGithubFeed() throws Exception {
        restGithubFeedMockMvc.perform(get("/api/githubFeeds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
        verify(githubFeedRepository).findOne(Long.MAX_VALUE);
    }

    @Test
    public void updateGithubFeed() throws Exception {
        existingGithubFeed.setRepositoryURL(UPDATED_REPOSITORY_URL);

        restGithubFeedMockMvc.perform(put("/api/githubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingGithubFeed)))
                .andExpect(status().isOk());
        verify(githubFeedRepository).save(existingGithubFeed);
    }

    @Test
    public void deleteGithubFeed() throws Exception {
        restGithubFeedMockMvc.perform(delete("/api/githubFeeds/{id}", existingGithubFeed.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        verify(githubFeedRepository).delete(existingGithubFeed.getId());
    }
}
