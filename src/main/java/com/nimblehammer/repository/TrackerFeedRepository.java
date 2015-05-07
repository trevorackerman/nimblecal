package com.nimblehammer.repository;

import com.nimblehammer.domain.TrackerFeed;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TrackerFeed entity.
 */
public interface TrackerFeedRepository extends JpaRepository<TrackerFeed,Long> {

}
