package com.nimblehammer.repository;

import com.nimblehammer.domain.GitHubFeed;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the GithubFeed entity.
 */
public interface GitHubFeedRepository extends JpaRepository<GitHubFeed,Long> {

}
