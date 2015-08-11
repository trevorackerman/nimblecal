package com.nimblehammer.repository;

import com.nimblehammer.domain.GitHubFeed;
import com.nimblehammer.domain.ProjectFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the GithubFeed entity.
 */
public interface GitHubFeedRepository extends JpaRepository<GitHubFeed,Long> {
    List<GitHubFeed> findByProjectFeed(ProjectFeed projectFeed);
}
