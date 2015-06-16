package com.nimblehammer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nimblehammer.domain.CalendarEvent;
import com.nimblehammer.domain.TrackerActivity;
import com.nimblehammer.domain.TrackerFeed;
import com.nimblehammer.domain.util.CalendarEventFactory;
import com.nimblehammer.repository.TrackerFeedRepository;
import com.nimblehammer.service.TrackerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TrackerFeed.
 */
@RestController
@RequestMapping("/api")
public class TrackerFeedResource {

    private final Logger log = LoggerFactory.getLogger(TrackerFeedResource.class);

    @Inject
    private TrackerFeedRepository trackerFeedRepository;

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private CalendarEventFactory calendarEventFactory;

    /**
     * POST  /trackerFeeds -> Create a new trackerFeed.
     */
    @RequestMapping(value = "/trackerFeeds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody TrackerFeed trackerFeed) throws URISyntaxException {
        log.debug("REST request to save TrackerFeed : {}", trackerFeed);
        if (trackerFeed.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new trackerFeed cannot already have an ID").build();
        }
        trackerFeedRepository.save(trackerFeed);
        return ResponseEntity.created(new URI("/api/trackerFeeds/" + trackerFeed.getId())).build();
    }

    /**
     * PUT  /trackerFeeds -> Updates an existing trackerFeed.
     */
    @RequestMapping(value = "/trackerFeeds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody TrackerFeed trackerFeed) throws URISyntaxException {
        log.debug("REST request to update TrackerFeed : {}", trackerFeed);
        if (trackerFeed.getId() == null) {
            return create(trackerFeed);
        }
        trackerFeedRepository.save(trackerFeed);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /trackerFeeds -> get all the trackerFeeds.
     */
    @RequestMapping(value = "/trackerFeeds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TrackerFeed> getAll() {
        log.debug("REST request to get all TrackerFeeds");
        final List<TrackerFeed> trackerFeeds = trackerFeedRepository.findAll();

        for (TrackerFeed trackerFeed : trackerFeeds) {
            if (trackerFeed.getProjectFeed() != null) {
                trackerFeed.getProjectFeed().setTrackerFeeds(null);
            }
        }

        return trackerFeeds;
    }

    /**
     * GET  /trackerFeeds/:id -> get the "id" trackerFeed.
     */
    @RequestMapping(value = "/trackerFeeds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TrackerFeed> get(@PathVariable Long id) {
        log.debug("REST request to get TrackerFeed : {}", id);
        return Optional.ofNullable(trackerFeedRepository.findOne(id))
            .map(trackerFeed -> new ResponseEntity<>(
                trackerFeed,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trackerFeeds/:id -> delete the "id" trackerFeed.
     */
    @RequestMapping(value = "/trackerFeeds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete TrackerFeed : {}", id);
        trackerFeedRepository.delete(id);
    }

    @RequestMapping(value = "/trackerFeeds/{id}/events",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CalendarEvent>> getEvents(@PathVariable String id, @RequestHeader("X-TrackerToken") String trackerToken) {
        TrackerFeed trackerFeed = trackerFeedRepository.findOne(new Long(id));

        if (trackerFeed == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<CalendarEvent> calendarEvents = new ArrayList<>();

        List<TrackerActivity> trackerActivities = trackerService.getProjectActivities(id, trackerToken);

        for (TrackerActivity trackerActivity : trackerActivities) {
            calendarEvents.add(calendarEventFactory.create(trackerActivity));
        }


        // Make some tests. Have the guest login associated with some example project feeds which have some public
        // tracker projects


        return new ResponseEntity<>(calendarEvents, HttpStatus.OK);
    }
}
