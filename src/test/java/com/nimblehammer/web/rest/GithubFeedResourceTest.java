package com.nimblehammer.web.rest;

import com.nimblehammer.domain.GithubFeed;
import com.nimblehammer.domain.github.*;
import com.nimblehammer.domain.util.CalendarEventFactory;
import com.nimblehammer.repository.GithubFeedRepository;
import com.nimblehammer.service.GitHubService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Mock
    private GitHubService githubService;

    @Mock
    private CalendarEventFactory calendarEventFactory;

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

    @Test
    public void getGithubEvents() throws Exception {
        GitHubAuthor johnWayne = new GitHubAuthor();
        johnWayne.setName("John Wayne");
        johnWayne.setEmail("jwayne@example.com");

        GitHubCommit gitHubCommit1 = new GitHubCommit();
        gitHubCommit1.setAuthor(johnWayne);
        gitHubCommit1.setSha("056f1f86e513c13feb6a2c31f8a4c91d4c856018");
        gitHubCommit1.setMessage("Reformat this code pilgrim!");
        gitHubCommit1.setUrl("https://api.github.com/repos/projectx/johnwayne/commits/056f1f86e513c13feb6a2c31f8a4c91d4c856018");

        GitHubPayload gitHubPayload = new GitHubPayload();
        gitHubPayload.setSize(1);
        gitHubPayload.setDistinct_size(1);
        gitHubPayload.setBefore("ca31e5b10850d765b392952dbe4a74b1c6cab47d");
        gitHubPayload.setHead("056f1f86e513c13feb6a2c31f8a4c91d4c856018");
        gitHubPayload.setRef("refs/heads/master");
        gitHubPayload.setCommits(Arrays.asList(gitHubCommit1));

        GitHubActor actor = new GitHubActor();
        actor.setId(100);
        actor.setLogin("johnwayne");
        actor.setUrl("https://api.github.com/users/johnwayne");
        actor.setAvatar_url("https://avatars.githubusercontent.com/u/100");

        GitHubEvent gitHubEvent1 = new GitHubEvent();
        DateTimeFormatter f = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());
        gitHubEvent1.setCreated_at(Instant.parse("2015-07-06T12:00:00Z"));
        gitHubEvent1.setActor(actor);
        gitHubEvent1.setId("111");
        gitHubEvent1.setPublic(true);
        gitHubEvent1.setType("PushEvent");
        gitHubEvent1.setPayload(gitHubPayload);

        List<GitHubEvent> gitHubEvents = Arrays.asList(gitHubEvent1);
        when(githubService.getRepositoryEvents("johnwayne", "projectx")).thenReturn(gitHubEvents);
        when(calendarEventFactory.create(gitHubEvent1)).thenCallRealMethod();
        when(calendarEventFactory.create(gitHubEvent1, gitHubCommit1)).thenCallRealMethod();

        restGithubFeedMockMvc.perform(get("/api/githubFeeds/{id}/events", existingGithubFeed.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[*].title").value("John Wayne committed 056f1f86e513c13feb6a2c31f8a4c91d4c856018"))
            .andExpect(jsonPath("$.[*].message").value("Reformat this code pilgrim!"))
            .andExpect(jsonPath("$.[*].description").value("Reformat this code pilgrim!"))
            .andExpect(jsonPath("$.[*].avatarUrl").value("https://avatars.githubusercontent.com/u/100"))
            .andExpect(jsonPath("$.[*].start").value("2015-07-06T12:00:00"));

        verify(githubFeedRepository).findOne(100L);
        verify(githubService).getRepositoryEvents("johnwayne", "projectx");
        verify(calendarEventFactory).create(gitHubEvent1);
        verify(calendarEventFactory).create(gitHubEvent1, gitHubCommit1);
    }
}
