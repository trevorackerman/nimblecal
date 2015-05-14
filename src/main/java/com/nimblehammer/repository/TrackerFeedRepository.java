package com.nimblehammer.repository;

import com.nimblehammer.domain.Calendar;
import com.nimblehammer.domain.TrackerFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the TrackerFeed entity.
 */
public interface TrackerFeedRepository extends JpaRepository<TrackerFeed,Long> {
    List<TrackerFeed> findByCalendar(Calendar calendar);
}
