package com.nimblehammer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nimblehammer.domain.GitHubFeed;
import com.nimblehammer.domain.ProjectFeed;
import com.nimblehammer.domain.TrackerFeed;
import com.nimblehammer.repository.ProjectFeedRepository;
import com.nimblehammer.repository.TrackerFeedRepository;
import com.nimblehammer.service.ProjectFeedService;
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
import java.util.List;

/**
 * REST controller for managing ProjectFeed.
 */
@RestController
@RequestMapping("/api")
public class ProjectFeedResource {

    private final Logger log = LoggerFactory.getLogger(ProjectFeedResource.class);

    @Autowired
    private ProjectFeedRepository projectFeedRepository;

    @Autowired
    private TrackerFeedRepository trackerFeedRepository;

    @Autowired
    private ProjectFeedService projectFeedService;

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

        if (projectFeed.getOwner() == null) {
            return ResponseEntity.badRequest().header("Failure", "A new projectfeed must have an owner").build();
        }
        projectFeedService.save(projectFeed);
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
        projectFeedService.update(projectFeed);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /projectfeeds -> get all the projectfeeds.
     */
    @RequestMapping(value = "/projectfeeds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectFeed>> getAll() {
        log.debug("REST request to get all Projectfeeds");
        List<ProjectFeed> projectFeeds = projectFeedService.getAllForCurrentUser();

        // Prevent infinite loop when generating json
        for (ProjectFeed projectFeed : projectFeeds) {
            List<TrackerFeed> trackerFeeds = projectFeed.getTrackerFeeds();
            for (TrackerFeed trackerFeed : trackerFeeds) {
                trackerFeed.setProjectFeed(null);
            }

            List<GitHubFeed> gitHubFeeds = projectFeed.getGitHubFeeds();
            for (GitHubFeed gitHubFeed : gitHubFeeds) {
                gitHubFeed.setProjectFeed(null);
            }
        }

        return new ResponseEntity<>(projectFeeds, HttpStatus.OK);
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

        ProjectFeed projectFeed = projectFeedService.get(id);

        if (projectFeed == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        for (TrackerFeed trackerFeed : projectFeed.getTrackerFeeds()) {
            if (trackerFeed.getProjectFeed() != null) {
                trackerFeed.getProjectFeed().setTrackerFeeds(null);
            }
        }

        return new ResponseEntity<>(projectFeed, HttpStatus.OK);
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
        projectFeedService.delete(id);
    }


    @RequestMapping(value = "/projectfeeds/{id}/trackerFeeds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TrackerFeed> getTrackerFeeds(@PathVariable Long id) {
        return trackerFeedRepository.findByProjectFeed(projectFeedRepository.findOne(id));
    }
}
