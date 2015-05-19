package com.nimblehammer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nimblehammer.domain.ProjectFeed;
import com.nimblehammer.domain.TrackerFeed;
import com.nimblehammer.repository.ProjectFeedRepository;
import com.nimblehammer.repository.TrackerFeedRepository;
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
 * REST controller for managing ProjectFeed.
 */
@RestController
@RequestMapping("/api")
public class ProjectFeedResource {

    private final Logger log = LoggerFactory.getLogger(ProjectFeedResource.class);

    @Inject
    private ProjectFeedRepository projectFeedRepository;

    @Inject
    private TrackerFeedRepository trackerFeedRepository;

    /**
     * POST  /projectfeeds -> Create a new projectfeed.
     */
    @RequestMapping(value = "/projectfeeds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ProjectFeed projectFeed) throws URISyntaxException {
        log.debug("REST request to save Projectfeed : {}", projectFeed);
        if (projectFeed.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new projectfeed cannot already have an ID").build();
        }
        projectFeedRepository.save(projectFeed);
        return ResponseEntity.created(new URI("/api/projectfeeds/" + projectFeed.getId())).build();
    }

    /**
     * PUT  /projectfeeds -> Updates an existing projectfeed.
     */
    @RequestMapping(value = "/projectfeeds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ProjectFeed projectFeed) throws URISyntaxException {
        log.debug("REST request to update Projectfeed : {}", projectFeed);
        if (projectFeed.getId() == null) {
            return create(projectFeed);
        }
        projectFeedRepository.save(projectFeed);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /projectfeeds -> get all the projectfeeds.
     */
    @RequestMapping(value = "/projectfeeds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjectFeed> getAll() {
        log.debug("REST request to get all Projectfeeds");
        return projectFeedRepository.findAll();
    }

    /**
     * GET  /projectfeeds/:id -> get the "id" projectfeed.
     */
    @RequestMapping(value = "/projectfeeds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectFeed> get(@PathVariable Long id) {
        log.debug("REST request to get Projectfeed : {}", id);
        return Optional.ofNullable(projectFeedRepository.findOne(id))
            .map(projectfeed -> new ResponseEntity<>(
                projectfeed,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /projectfeeds/:id -> delete the "id" projectfeed.
     */
    @RequestMapping(value = "/projectfeeds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Projectfeed : {}", id);
        projectFeedRepository.delete(id);
    }


    @RequestMapping(value = "/projectfeeds/{id}/trackerFeeds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TrackerFeed> getTrackerFeeds(@PathVariable Long id) {
        return trackerFeedRepository.findByProjectFeed(projectFeedRepository.findOne(id));
    }
}
