package com.nimblehammer.repository;

import com.nimblehammer.domain.GithubFeed;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GithubFeed entity.
 */
public interface GithubFeedRepository extends JpaRepository<GithubFeed,Long> {

}
