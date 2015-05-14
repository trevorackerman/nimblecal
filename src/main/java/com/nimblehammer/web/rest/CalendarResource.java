package com.nimblehammer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nimblehammer.domain.Calendar;
import com.nimblehammer.domain.TrackerFeed;
import com.nimblehammer.repository.CalendarRepository;
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
 * REST controller for managing Calendar.
 */
@RestController
@RequestMapping("/api")
public class CalendarResource {

    private final Logger log = LoggerFactory.getLogger(CalendarResource.class);

    @Inject
    private CalendarRepository calendarRepository;

    @Inject
    private TrackerFeedRepository trackerFeedRepository;

    /**
     * POST  /calendars -> Create a new calendar.
     */
    @RequestMapping(value = "/calendars",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Calendar calendar) throws URISyntaxException {
        log.debug("REST request to save Calendar : {}", calendar);
        if (calendar.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new calendar cannot already have an ID").build();
        }
        calendarRepository.save(calendar);
        return ResponseEntity.created(new URI("/api/calendars/" + calendar.getId())).build();
    }

    /**
     * PUT  /calendars -> Updates an existing calendar.
     */
    @RequestMapping(value = "/calendars",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Calendar calendar) throws URISyntaxException {
        log.debug("REST request to update Calendar : {}", calendar);
        if (calendar.getId() == null) {
            return create(calendar);
        }
        calendarRepository.save(calendar);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /calendars -> get all the calendars.
     */
    @RequestMapping(value = "/calendars",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Calendar> getAll() {
        log.debug("REST request to get all Calendars");
        return calendarRepository.findAll();
    }

    /**
     * GET  /calendars/:id -> get the "id" calendar.
     */
    @RequestMapping(value = "/calendars/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Calendar> get(@PathVariable Long id) {
        log.debug("REST request to get Calendar : {}", id);
        return Optional.ofNullable(calendarRepository.findOne(id))
            .map(calendar -> new ResponseEntity<>(
                calendar,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /calendars/:id -> delete the "id" calendar.
     */
    @RequestMapping(value = "/calendars/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Calendar : {}", id);
        calendarRepository.delete(id);
    }


    @RequestMapping(value = "/calendars/{id}/trackerFeeds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TrackerFeed> getTrackerFeeds(@PathVariable Long id) {
        return trackerFeedRepository.findByCalendar(calendarRepository.findOne(id));
    }
}
