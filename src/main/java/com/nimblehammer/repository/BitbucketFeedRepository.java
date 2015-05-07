package com.nimblehammer.repository;

import com.nimblehammer.domain.BitbucketFeed;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BitbucketFeed entity.
 */
public interface BitbucketFeedRepository extends JpaRepository<BitbucketFeed,Long> {

}
