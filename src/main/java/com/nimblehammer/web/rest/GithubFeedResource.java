package com.nimblehammer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nimblehammer.domain.CalendarEvent;
import com.nimblehammer.domain.GithubFeed;
import com.nimblehammer.domain.github.GitHubEvent;
import com.nimblehammer.domain.util.CalendarEventFactory;
import com.nimblehammer.repository.GithubFeedRepository;
import com.nimblehammer.service.GitHubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GithubFeed.
 */
@RestController
@RequestMapping("/api")
public class GithubFeedResource {

    private final Logger log = LoggerFactory.getLogger(GithubFeedResource.class);

    @Autowired
    private GitHubService gitHubService;

    private CalendarEventFactory calendarEventFactory = new CalendarEventFactory();

    @Autowired
    private GithubFeedRepository githubFeedRepository;

    /**
     * POST  /githubFeeds -> Create a new githubFeed.
     */
    @RequestMapping(value = "/githubFeeds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody GithubFeed githubFeed) throws URISyntaxException {
        log.debug("REST request to save GithubFeed : {}", githubFeed);
        if (githubFeed.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new githubFeed cannot already have an ID").build();
        }
        githubFeedRepository.save(githubFeed);
        return ResponseEntity.created(new URI("/api/githubFeeds/" + githubFeed.getId())).build();
    }

    /**
     * PUT  /githubFeeds -> Updates an existing githubFeed.
     */
    @RequestMapping(value = "/githubFeeds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody GithubFeed githubFeed) throws URISyntaxException {
        log.debug("REST request to update GithubFeed : {}", githubFeed);
        if (githubFeed.getId() == null) {
            return create(githubFeed);
        }
        githubFeedRepository.save(githubFeed);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /githubFeeds -> get all the githubFeeds.
     */
    @RequestMapping(value = "/githubFeeds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GithubFeed> getAll() {
        log.debug("REST request to get all GithubFeeds");
        return githubFeedRepository.findAll();
    }

    /**
     * GET  /githubFeeds/:id -> get the "id" githubFeed.
     */
    @RequestMapping(value = "/githubFeeds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GithubFeed> get(@PathVariable Long id) {
        log.debug("REST request to get GithubFeed : {}", id);
        return Optional.ofNullable(githubFeedRepository.findOne(id))
            .map(githubFeed -> new ResponseEntity<>(
                githubFeed,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /githubFeeds/:id -> delete the "id" githubFeed.
     */
    @RequestMapping(value = "/githubFeeds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete GithubFeed : {}", id);
        githubFeedRepository.delete(id);
    }

    @RequestMapping(value = "/githubFeeds/{id}/events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CalendarEvent> getEvents(@PathVariable String id) {

        GithubFeed githubFeed = githubFeedRepository.findOne(new Long(id));

        if (githubFeed == null) {
            return null;
        }

        List<CalendarEvent> calendarEvents = new ArrayList<>();

        List<GitHubEvent> events = gitHubService.getRepositoryEvents(githubFeed.getRepositoryOwner(), githubFeed.getRepositoryName());

        for (GitHubEvent event : events) {
            calendarEvents.addAll(calendarEventFactory.create(event));
        }

        return calendarEvents;
    }
}
