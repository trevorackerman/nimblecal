package com.nimblehammer.web.rest;

import com.nimblehammer.domain.GitHubFeed;
import com.nimblehammer.domain.gitHub.*;
import com.nimblehammer.domain.util.CalendarEventFactory;
import com.nimblehammer.repository.GitHubFeedRepository;
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

public class GitHubFeedResourceTest {

    private static final String UPDATED_REPOSITORY_URL = "UPDATED_TEXT";

    @Mock
    private GitHubFeedRepository gitHubFeedRepository;

    @Mock
    private GitHubService githubService;

    @Mock
    private CalendarEventFactory calendarEventFactory;

    private MockMvc restGithubFeedMockMvc;

    @InjectMocks
    private GitHubFeedResource gitHubFeedResource;

    private GitHubFeed existingGitHubFeed;

    private List<GitHubFeed> gitHubFeeds = new ArrayList<>();

    @Before
    public void before() {
        initMocks(this);
        this.restGithubFeedMockMvc = MockMvcBuilders.standaloneSetup(gitHubFeedResource).build();

        existingGitHubFeed = new GitHubFeed();
        existingGitHubFeed.setRepositoryURL("https://www.example.com/johnwayne/projectx");
        existingGitHubFeed.setRepositoryName("projectx");
        existingGitHubFeed.setRepositoryOwner("johnwayne");
        existingGitHubFeed.setId(100L);

        gitHubFeeds.add(existingGitHubFeed);

        when(gitHubFeedRepository.findOne(100L)).thenReturn(existingGitHubFeed);
        when(gitHubFeedRepository.findAll()).thenReturn(gitHubFeeds);
    }

    @Test
    public void createGithubFeed() throws Exception {
        GitHubFeed gitHubFeed = new GitHubFeed();
        gitHubFeed.setRepositoryName("javaproject");
        gitHubFeed.setRepositoryOwner("duke");
        gitHubFeed.setRepositoryURL("https://example.com/duke/javaproject");

        restGithubFeedMockMvc.perform(post("/api/gitHubFeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gitHubFeed)))
                .andExpect(status().isCreated());

        verify(gitHubFeedRepository).save(gitHubFeed);
    }

    @Test
    public void checkRepositoryURLIsRequired() throws Exception {
        GitHubFeed gitHubFeed = new GitHubFeed();
        gitHubFeed.setRepositoryName("javaproject");
        gitHubFeed.setRepositoryOwner("duke");
        gitHubFeed.setRepositoryURL(null);

        restGithubFeedMockMvc.perform(post("/api/gitHubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gitHubFeed)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(gitHubFeedRepository);
    }

    @Test
    public void checkRepositoryNameIsRequired() throws Exception {
        GitHubFeed gitHubFeed = new GitHubFeed();
        gitHubFeed.setRepositoryName(null);
        gitHubFeed.setRepositoryOwner("duke");
        gitHubFeed.setRepositoryURL("https://example.com/duke/javaproject");

        restGithubFeedMockMvc.perform(post("/api/gitHubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gitHubFeed)))
            .andExpect(status().isBadRequest());

        verifyZeroInteractions(gitHubFeedRepository);
    }

    @Test
    public void checkRepositoryOwnerIsRequired() throws Exception {
        GitHubFeed gitHubFeed = new GitHubFeed();
        gitHubFeed.setRepositoryName("javaproject");
        gitHubFeed.setRepositoryOwner(null);
        gitHubFeed.setRepositoryURL("https://example.com/duke/javaproject");

        restGithubFeedMockMvc.perform(post("/api/gitHubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gitHubFeed)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(gitHubFeedRepository);
    }

    @Test
    public void getAllGithubFeeds() throws Exception {
        restGithubFeedMockMvc.perform(get("/api/gitHubFeeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(existingGitHubFeed.getId().intValue())))
                .andExpect(jsonPath("$.[*].repositoryURL").value(hasItem("https://www.example.com/johnwayne/projectx")));
        verify(gitHubFeedRepository).findAll();
    }

    @Test
    public void getGithubFeed() throws Exception {
        restGithubFeedMockMvc.perform(get("/api/gitHubFeeds/{id}", existingGitHubFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(existingGitHubFeed.getId().intValue()))
            .andExpect(jsonPath("$.repositoryURL").value("https://www.example.com/johnwayne/projectx"));

        verify(gitHubFeedRepository).findOne(existingGitHubFeed.getId());
    }

    @Test
    public void getNonExistingGithubFeed() throws Exception {
        restGithubFeedMockMvc.perform(get("/api/gitHubFeeds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
        verify(gitHubFeedRepository).findOne(Long.MAX_VALUE);
    }

    @Test
    public void updateGithubFeed() throws Exception {
        existingGitHubFeed.setRepositoryURL(UPDATED_REPOSITORY_URL);

        restGithubFeedMockMvc.perform(put("/api/gitHubFeeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingGitHubFeed)))
                .andExpect(status().isOk());
        verify(gitHubFeedRepository).save(existingGitHubFeed);
    }

    @Test
    public void deleteGithubFeed() throws Exception {
        restGithubFeedMockMvc.perform(delete("/api/gitHubFeeds/{id}", existingGitHubFeed.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        verify(gitHubFeedRepository).delete(existingGitHubFeed.getId());
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

        restGithubFeedMockMvc.perform(get("/api/gitHubFeeds/{id}/events", existingGitHubFeed.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[*].title").value("John Wayne committed 056f1f86e513c13feb6a2c31f8a4c91d4c856018"))
            .andExpect(jsonPath("$.[*].message").value("Reformat this code pilgrim!"))
            .andExpect(jsonPath("$.[*].description").value("Reformat this code pilgrim!"))
            .andExpect(jsonPath("$.[*].avatarUrl").value("https://avatars.githubusercontent.com/u/100"))
            .andExpect(jsonPath("$.[*].start").value("2015-07-06T12:00:00"));

        verify(gitHubFeedRepository).findOne(100L);
        verify(githubService).getRepositoryEvents("johnwayne", "projectx");
        verify(calendarEventFactory).create(gitHubEvent1);
        verify(calendarEventFactory).create(gitHubEvent1, gitHubCommit1);
    }
}
