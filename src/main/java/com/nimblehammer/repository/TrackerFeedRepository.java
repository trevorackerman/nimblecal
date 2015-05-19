package com.nimblehammer.repository;

import com.nimblehammer.domain.ProjectFeed;
import com.nimblehammer.domain.TrackerFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the TrackerFeed entity.
 */
public interface TrackerFeedRepository extends JpaRepository<TrackerFeed,Long> {
    List<TrackerFeed> findByProjectFeed(ProjectFeed projectFeed);
}
