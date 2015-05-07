package com.nimblehammer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nimblehammer.domain.JiraFeed;
import com.nimblehammer.repository.JiraFeedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing JiraFeed.
 */
@RestController
@RequestMapping("/api")
public class JiraFeedResource {

    private final Logger log = LoggerFactory.getLogger(JiraFeedResource.class);

    @Inject
    private JiraFeedRepository jiraFeedRepository;

    /**
     * POST  /jiraFeeds -> Create a new jiraFeed.
     */
    @RequestMapping(value = "/jiraFeeds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody JiraFeed jiraFeed) throws URISyntaxException {
        log.debug("REST request to save JiraFeed : {}", jiraFeed);
        if (jiraFeed.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new jiraFeed cannot already have an ID").build();
        }
        jiraFeedRepository.save(jiraFeed);
        return ResponseEntity.created(new URI("/api/jiraFeeds/" + jiraFeed.getId())).build();
    }

    /**
     * PUT  /jiraFeeds -> Updates an existing jiraFeed.
     */
    @RequestMapping(value = "/jiraFeeds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody JiraFeed jiraFeed) throws URISyntaxException {
        log.debug("REST request to update JiraFeed : {}", jiraFeed);
        if (jiraFeed.getId() == null) {
            return create(jiraFeed);
        }
        jiraFeedRepository.save(jiraFeed);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /jiraFeeds -> get all the jiraFeeds.
     */
    @RequestMapping(value = "/jiraFeeds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JiraFeed> getAll() {
        log.debug("REST request to get all JiraFeeds");
        return jiraFeedRepository.findAll();
    }

    /**
     * GET  /jiraFeeds/:id -> get the "id" jiraFeed.
     */
    @RequestMapping(value = "/jiraFeeds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JiraFeed> get(@PathVariable Long id) {
        log.debug("REST request to get JiraFeed : {}", id);
        return Optional.ofNullable(jiraFeedRepository.findOne(id))
            .map(jiraFeed -> new ResponseEntity<>(
                jiraFeed,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jiraFeeds/:id -> delete the "id" jiraFeed.
     */
    @RequestMapping(value = "/jiraFeeds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete JiraFeed : {}", id);
        jiraFeedRepository.delete(id);
    }
}
