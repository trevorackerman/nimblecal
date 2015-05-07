package com.nimblehammer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nimblehammer.domain.BitbucketFeed;
import com.nimblehammer.repository.BitbucketFeedRepository;
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
 * REST controller for managing BitbucketFeed.
 */
@RestController
@RequestMapping("/api")
public class BitbucketFeedResource {

    private final Logger log = LoggerFactory.getLogger(BitbucketFeedResource.class);

    @Inject
    private BitbucketFeedRepository bitbucketFeedRepository;

    /**
     * POST  /bitbucketFeeds -> Create a new bitbucketFeed.
     */
    @RequestMapping(value = "/bitbucketFeeds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody BitbucketFeed bitbucketFeed) throws URISyntaxException {
        log.debug("REST request to save BitbucketFeed : {}", bitbucketFeed);
        if (bitbucketFeed.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new bitbucketFeed cannot already have an ID").build();
        }
        bitbucketFeedRepository.save(bitbucketFeed);
        return ResponseEntity.created(new URI("/api/bitbucketFeeds/" + bitbucketFeed.getId())).build();
    }

    /**
     * PUT  /bitbucketFeeds -> Updates an existing bitbucketFeed.
     */
    @RequestMapping(value = "/bitbucketFeeds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody BitbucketFeed bitbucketFeed) throws URISyntaxException {
        log.debug("REST request to update BitbucketFeed : {}", bitbucketFeed);
        if (bitbucketFeed.getId() == null) {
            return create(bitbucketFeed);
        }
        bitbucketFeedRepository.save(bitbucketFeed);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /bitbucketFeeds -> get all the bitbucketFeeds.
     */
    @RequestMapping(value = "/bitbucketFeeds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BitbucketFeed> getAll() {
        log.debug("REST request to get all BitbucketFeeds");
        return bitbucketFeedRepository.findAll();
    }

    /**
     * GET  /bitbucketFeeds/:id -> get the "id" bitbucketFeed.
     */
    @RequestMapping(value = "/bitbucketFeeds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BitbucketFeed> get(@PathVariable Long id) {
        log.debug("REST request to get BitbucketFeed : {}", id);
        return Optional.ofNullable(bitbucketFeedRepository.findOne(id))
            .map(bitbucketFeed -> new ResponseEntity<>(
                bitbucketFeed,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bitbucketFeeds/:id -> delete the "id" bitbucketFeed.
     */
    @RequestMapping(value = "/bitbucketFeeds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete BitbucketFeed : {}", id);
        bitbucketFeedRepository.delete(id);
    }
}
