package com.nimblehammer.web.rest;

import com.nimblehammer.Application;
import com.nimblehammer.domain.Calendar;
import com.nimblehammer.repository.CalendarRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CalendarResource REST controller.
 *
 * @see CalendarResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CalendarResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";

    @Inject
    private CalendarRepository calendarRepository;

    private MockMvc restCalendarMockMvc;

    private Calendar calendar;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CalendarResource calendarResource = new CalendarResource();
        ReflectionTestUtils.setField(calendarResource, "calendarRepository", calendarRepository);
        this.restCalendarMockMvc = MockMvcBuilders.standaloneSetup(calendarResource).build();
    }

    @Before
    public void initTest() {
        calendar = new Calendar();
        calendar.setTitle(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createCalendar() throws Exception {
        int databaseSizeBeforeCreate = calendarRepository.findAll().size();

        // Create the Calendar
        restCalendarMockMvc.perform(post("/api/calendars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendar)))
                .andExpect(status().isCreated());

        // Validate the Calendar in the database
        List<Calendar> calendars = calendarRepository.findAll();
        assertThat(calendars).hasSize(databaseSizeBeforeCreate + 1);
        Calendar testCalendar = calendars.get(calendars.size() - 1);
        assertThat(testCalendar.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(calendarRepository.findAll()).hasSize(0);
        // set the field null
        calendar.setTitle(null);

        // Create the Calendar, which fails.
        restCalendarMockMvc.perform(post("/api/calendars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendar)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Calendar> calendars = calendarRepository.findAll();
        assertThat(calendars).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllCalendars() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendars
        restCalendarMockMvc.perform(get("/api/calendars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(calendar.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get the calendar
        restCalendarMockMvc.perform(get("/api/calendars/{id}", calendar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(calendar.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCalendar() throws Exception {
        // Get the calendar
        restCalendarMockMvc.perform(get("/api/calendars/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

		int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar
        calendar.setTitle(UPDATED_TITLE);
        restCalendarMockMvc.perform(put("/api/calendars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendar)))
                .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendars = calendarRepository.findAll();
        assertThat(calendars).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendars.get(calendars.size() - 1);
        assertThat(testCalendar.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void deleteCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

		int databaseSizeBeforeDelete = calendarRepository.findAll().size();

        // Get the calendar
        restCalendarMockMvc.perform(delete("/api/calendars/{id}", calendar.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Calendar> calendars = calendarRepository.findAll();
        assertThat(calendars).hasSize(databaseSizeBeforeDelete - 1);
    }
}
