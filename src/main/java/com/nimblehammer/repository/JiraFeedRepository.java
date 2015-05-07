package com.nimblehammer.repository;

import com.nimblehammer.domain.JiraFeed;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JiraFeed entity.
 */
public interface JiraFeedRepository extends JpaRepository<JiraFeed,Long> {

}
