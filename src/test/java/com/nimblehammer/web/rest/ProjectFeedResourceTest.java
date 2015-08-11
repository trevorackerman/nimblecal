package com.nimblehammer.web.rest;

import com.nimblehammer.Application;
import com.nimblehammer.domain.*;
import com.nimblehammer.repository.ProjectFeedRepository;
import com.nimblehammer.repository.TrackerFeedRepository;
import com.nimblehammer.service.ProjectFeedService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
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
    @Mock
    private ProjectFeedRepository projectFeedRepository;

    @Mock
    private TrackerFeedRepository trackerFeedRepository;

    @Mock
    private ProjectFeedService projectFeedService;

    @InjectMocks
    private ProjectFeedResource projectFeedResource;

    private MockMvc restProjectFeedMockMvc;

    @PostConstruct
    public void setup() {
        initMocks(this);
        this.restProjectFeedMockMvc = MockMvcBuilders.standaloneSetup(projectFeedResource).build();
    }

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    @Transactional
    public void createProjectFeed() throws Exception {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setTitle("Project without tracker feeds");

        User user = new User();
        user.setId(1L);
        user.setLogin("testguy");

        projectFeed.setOwner(user);

        restProjectFeedMockMvc.perform(post("/api/projectfeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectFeed)))
                .andExpect(status().isCreated());

        verify(projectFeedService).save(projectFeed);
    }

    @Test
    @Transactional
    public void itReturns400WhenNoOwner() throws Exception {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setTitle("Project without tracker feeds");

        restProjectFeedMockMvc.perform(post("/api/projectfeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectFeed)))
            .andExpect(status().isBadRequest())
        .andExpect(header().string("Failure", "A new projectfeed must have an owner"));
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setTitle(null);

        restProjectFeedMockMvc.perform(post("/api/projectfeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectFeed)))
                .andExpect(status().isBadRequest());

        verify(projectFeedRepository, never()).save(projectFeed);
    }

    @Test
    @Transactional
    public void getAllProjectFeeds() throws Exception {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setId(2345L);
        projectFeed.setTitle("blah");

        TrackerFeed trackerFeed = new TrackerFeed();
        trackerFeed.setId(1L);
        trackerFeed.setProjectId("442903");

        List<TrackerFeed> trackerFeeds = new ArrayList<>();
        trackerFeeds.add(trackerFeed);

        projectFeed.setTrackerFeeds(trackerFeeds);

        GitHubFeed githubFeed = new GitHubFeed();
        githubFeed.setId(314L);
        githubFeed.setRepositoryName("truegrit");
        githubFeed.setRepositoryOwner("johnwayne");
        githubFeed.setRepositoryURL("https://example.com/johnwayne/truegrit");

        List<GitHubFeed> githubFeeds = new ArrayList<>();
        githubFeeds.add(githubFeed);

        projectFeed.setGitHubFeeds(githubFeeds);

        List<ProjectFeed> projectFeeds = new ArrayList<>();
        projectFeeds.add(projectFeed);

        when(projectFeedService.getAllForCurrentUser()).thenReturn(projectFeeds);

        // Get all the projectFeeds
       restProjectFeedMockMvc.perform(get("/api/projectfeeds"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.[*].id").value(2345))
        .andExpect(jsonPath("$.[*].title").value(hasItem("blah")))
        .andExpect(jsonPath("$.[*].trackerFeeds[*].id").value(1))
        .andExpect(jsonPath("$.[*].trackerFeeds[*].projectId").value("442903"))
        .andExpect(jsonPath("$.[*].gitHubFeeds[*].id").value(314))
        .andExpect(jsonPath("$.[*].gitHubFeeds[*].repositoryName").value("truegrit"))
        .andExpect(jsonPath("$.[*].gitHubFeeds[*].repositoryOwner").value("johnwayne"))
        .andExpect(jsonPath("$.[*].gitHubFeeds[*].repositoryURL").value("https://example.com/johnwayne/truegrit"));
    }

    @Test
    @Transactional
    public void getProjectFeed() throws Exception {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setTitle("this project feed");
        projectFeed.setId(1234L);

        when(projectFeedService.get(1234L)).thenReturn(projectFeed);

        // Get the projectFeed
        restProjectFeedMockMvc.perform(get("/api/projectfeeds/{id}", 1234L))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1234))
            .andExpect(jsonPath("$.title").value("this project feed"));
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
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setId(1L);
        projectFeed.setTitle("the title was changed");
        projectFeed.getTrackerFeeds().clear();

        restProjectFeedMockMvc.perform(put("/api/projectfeeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectFeed)))
                .andExpect(status().isOk());

        verify(projectFeedService).update(projectFeed);
    }

    @Test
    @Transactional
    public void deleteProjectFeed() throws Exception {
        restProjectFeedMockMvc.perform(delete("/api/projectfeeds/{id}", 3456L)
            .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        verify(projectFeedService).delete(3456L);
    }
}
