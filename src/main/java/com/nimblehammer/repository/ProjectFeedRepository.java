package com.nimblehammer.repository;

import com.nimblehammer.domain.ProjectFeed;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectFeed entity.
 */
public interface ProjectFeedRepository extends JpaRepository<ProjectFeed,Long> {

    @Query("select projectfeed from ProjectFeed projectfeed where projectfeed.user.login = ?#{principal.username}")
    List<ProjectFeed> findAllForCurrentUser();

}
