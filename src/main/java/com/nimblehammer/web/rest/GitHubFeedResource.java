package com.nimblehammer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nimblehammer.domain.CalendarEvent;
import com.nimblehammer.domain.GitHubFeed;
import com.nimblehammer.domain.gitHub.GitHubEvent;
import com.nimblehammer.domain.util.CalendarEventFactory;
import com.nimblehammer.repository.GitHubFeedRepository;
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
 * REST controller for managing GitHubFeed.
 */
@RestController
@RequestMapping("/api")
public class GitHubFeedResource {

    private final Logger log = LoggerFactory.getLogger(GitHubFeedResource.class);

    @Autowired
    private GitHubService gitHubService;

    private CalendarEventFactory calendarEventFactory = new CalendarEventFactory();

    @Autowired
    private GitHubFeedRepository gitHubFeedRepository;

    /**
     * POST  /gitHubFeeds -> Create a new gitHubFeed.
     */
    @RequestMapping(value = "/gitHubFeeds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody GitHubFeed gitHubFeed) throws URISyntaxException {
        log.debug("REST request to save GitHubFeed : {}", gitHubFeed);
        if (gitHubFeed.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new gitHubFeed cannot already have an ID").build();
        }
        gitHubFeedRepository.save(gitHubFeed);
        return ResponseEntity.created(new URI("/api/gitHubFeeds/" + gitHubFeed.getId())).build();
    }

    /**
     * PUT  /gitHubFeeds -> Updates an existing gitHubFeed.
     */
    @RequestMapping(value = "/gitHubFeeds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody GitHubFeed gitHubFeed) throws URISyntaxException {
        log.debug("REST request to update GitHubFeed : {}", gitHubFeed);
        if (gitHubFeed.getId() == null) {
            return create(gitHubFeed);
        }
        gitHubFeedRepository.save(gitHubFeed);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /gitHubFeeds -> get all the gitHubFeeds.
     */
    @RequestMapping(value = "/gitHubFeeds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GitHubFeed> getAll() {
        log.debug("REST request to get all GitHubFeeds");
        return gitHubFeedRepository.findAll();
    }

    /**
     * GET  /gitHubFeeds/:id -> get the "id" gitHubFeed.
     */
    @RequestMapping(value = "/gitHubFeeds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GitHubFeed> get(@PathVariable Long id) {
        log.debug("REST request to get GitHubFeed : {}", id);
        return Optional.ofNullable(gitHubFeedRepository.findOne(id))
            .map(gitHubFeed -> new ResponseEntity<>(
                gitHubFeed,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gitHubFeeds/:id -> delete the "id" gitHubFeed.
     */
    @RequestMapping(value = "/gitHubFeeds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete GitHubFeed : {}", id);
        gitHubFeedRepository.delete(id);
    }

    @RequestMapping(value = "/gitHubFeeds/{id}/events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CalendarEvent> getEvents(@PathVariable String id) {

        GitHubFeed gitHubFeed = gitHubFeedRepository.findOne(new Long(id));

        if (gitHubFeed == null) {
            return null;
        }

        List<CalendarEvent> calendarEvents = new ArrayList<>();

        List<GitHubEvent> events = gitHubService.getRepositoryEvents(gitHubFeed.getRepositoryOwner(), gitHubFeed.getRepositoryName());

        for (GitHubEvent event : events) {
            calendarEvents.addAll(calendarEventFactory.create(event));
        }

        return calendarEvents;
    }
}
