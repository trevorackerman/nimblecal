package com.nimblehammer.service;

import com.nimblehammer.domain.gitHub.GitHubEvent;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class GitHubService {
    private RestTemplate restTemplate = new RestTemplate();

    public List<GitHubEvent> getRepositoryEvents(String owner, String repository) {
        URI uri = URI.create("https://api.github.com/repos/" + owner + "/" + repository + "/events");
        GitHubEvent[] gitHubEvents = restTemplate.getForObject(uri, GitHubEvent[].class);
        for (GitHubEvent gitHubEvent : gitHubEvents) {
            gitHubEvent.setRepositoryName(repository);
            gitHubEvent.setRepositoryOwner(owner);
        }
        return Arrays.asList(gitHubEvents);
    }
}
