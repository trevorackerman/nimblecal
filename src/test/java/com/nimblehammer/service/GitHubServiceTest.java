package com.nimblehammer.service;

import com.nimblehammer.domain.gitHub.GitHubEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GitHubServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    GitHubService gitHubService;

    @Before
    public void before() {
        initMocks(this);

        GitHubEvent gitHubEvent = new GitHubEvent();
        gitHubEvent.setId("1234567890");
        gitHubEvent.setType("PushEvent");
        GitHubEvent[] gitHubEvents = new GitHubEvent[] { gitHubEvent };

        URI uri = URI.create("https://api.github.com/repos/bobsmith/projectrepo/events");

        when(restTemplate.getForObject(uri, GitHubEvent[].class)).thenReturn(gitHubEvents);
    }

    @Test
    public void itCallsGitHubEventsAPI() {
        List<GitHubEvent> gitHubEvents = gitHubService.getRepositoryEvents("bobsmith", "projectrepo");
        assertThat(gitHubEvents.size(), equalTo(1));
        assertThat(gitHubEvents.get(0).getId(), equalTo("1234567890"));
        assertThat(gitHubEvents.get(0).getType(), equalTo("PushEvent"));
    }
}
