package com.nimblehammer.service;

import org.junit.Test;

import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class DateTimeServiceTest {

    @Test
    public void itSetsTheDateToOneAndHMSMsecToZero() {

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime beginningOfMonth = new DateTimeService().getBeginningOfMonth();
        assertThat(beginningOfMonth.getYear(), equalTo(now.getYear()));
        assertThat(beginningOfMonth.getMonth(), equalTo(now.getMonth()));
        assertThat(beginningOfMonth.getDayOfMonth(), equalTo(1));
        assertThat(beginningOfMonth.getHour(), equalTo(0));
        assertThat(beginningOfMonth.getMinute(), equalTo(0));
        assertThat(beginningOfMonth.getSecond(), equalTo(0));
        assertThat(beginningOfMonth.getNano(), equalTo(0));
    }

    @Test
    public void itPrintsCorrectISO8601Format() {
        assertThat(new DateTimeService().getISO8601BeginningOfMonth(), containsString("-01T00:00:00-"));
    }
}
