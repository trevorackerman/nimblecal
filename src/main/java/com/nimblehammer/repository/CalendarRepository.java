package com.nimblehammer.repository;

import com.nimblehammer.domain.Calendar;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Calendar entity.
 */
public interface CalendarRepository extends JpaRepository<Calendar,Long> {

    @Query("select calendar from Calendar calendar where calendar.user.login = ?#{principal.username}")
    List<Calendar> findAllForCurrentUser();

}
