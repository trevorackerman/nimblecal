package com.nimblehammer.service;

import com.nimblehammer.domain.ProjectFeed;
import com.nimblehammer.domain.TrackerFeed;
import com.nimblehammer.domain.User;
import com.nimblehammer.repository.ProjectFeedRepository;
import com.nimblehammer.repository.TrackerFeedRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProjectFeedServiceTest {
    @Mock
    private ProjectFeedRepository projectFeedRepository;

    @Mock
    private TrackerFeedRepository trackerFeedRepository;

    @InjectMocks
    private ProjectFeedService projectFeedService;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void itCreatesProjectFeed() {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setTitle("allan parsons project");

        TrackerFeed trackerFeed = new TrackerFeed();
        trackerFeed.setProjectId("abc123");

        User user = new User();
        user.setId(1L);

        projectFeed.getTrackerFeeds().add(trackerFeed);
        projectFeed.setOwner(user);

        projectFeedService.save(projectFeed);

        verify(projectFeedRepository).save(projectFeed);
        verify(trackerFeedRepository).save(trackerFeed);
    }

    @Test
    public void itThrowsIllegalArgumentExceptionWithoutOwner() {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setTitle("allan parsons project");

        TrackerFeed trackerFeed = new TrackerFeed();
        trackerFeed.setProjectId("abc123");

        projectFeed.getTrackerFeeds().add(trackerFeed);

        try {
            projectFeedService.save(projectFeed);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void itUpdatesProjectWithoutTrackerFeeds() {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setId(1L);
        projectFeed.setTitle("allan parsons project");

        when(trackerFeedRepository.findByProjectFeed(projectFeed)).thenReturn(new ArrayList<TrackerFeed>());

        projectFeedService.update(projectFeed);

        verify(projectFeedRepository).save(projectFeed);
        verify(trackerFeedRepository).findByProjectFeed(projectFeed);
        verify(trackerFeedRepository, never()).save(anyCollection());
        verify(trackerFeedRepository, never()).delete(anyCollection());
    }

    @Test
    public void itDeletesOrphanedTrackerFeeds() {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setId(1L);
        projectFeed.setTitle("project nice");

        TrackerFeed orphanedTrackerFeed = new TrackerFeed();
        orphanedTrackerFeed.setId(1L);
        orphanedTrackerFeed.setProjectId("111111");
//        orphanedTrackerFeed.setProjectFeed(projectFeed);

        List<TrackerFeed> existingTrackerFeeds = new ArrayList<>();
        existingTrackerFeeds.add(orphanedTrackerFeed);

        when(trackerFeedRepository.findByProjectFeed(projectFeed)).thenReturn(existingTrackerFeeds);

        projectFeedService.update(projectFeed);

        verify(trackerFeedRepository, never()).save(anyCollection());
        verify(trackerFeedRepository).delete(orphanedTrackerFeed);
    }

    @Test
    public void itAddsNewTrackerFeeds() {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setId(1L);
        projectFeed.setTitle("project nice");

        TrackerFeed newTrackerFeed = new TrackerFeed();
        newTrackerFeed.setProjectId("2222222");
//        newTrackerFeed.setProjectFeed(projectFeed);

        projectFeed.getTrackerFeeds().add(newTrackerFeed);

        when(trackerFeedRepository.findByProjectFeed(projectFeed)).thenReturn(new ArrayList<TrackerFeed>());

        projectFeedService.update(projectFeed);

        verify(trackerFeedRepository).save(newTrackerFeed);
        verify(trackerFeedRepository, never()).delete(anyCollection());
    }

    @Test
    public void itDeletesProjectFeeds() {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setId(1L);
        projectFeed.setTitle("project nice");

        TrackerFeed trackerFeed = new TrackerFeed();
        trackerFeed.setProjectId("2222222");
//        trackerFeed.setProjectFeed(projectFeed);

        List<TrackerFeed> trackerFeeds = new ArrayList<>();
        trackerFeeds.add(trackerFeed);

        when(trackerFeedRepository.findByProjectFeed(projectFeed)).thenReturn(trackerFeeds);

        projectFeedService.delete(projectFeed);

        verify(trackerFeedRepository).delete(trackerFeed);
        verify(projectFeedRepository).delete(projectFeed);
    }

    @Test
    public void itDeletesProjectFeedById() {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setId(1L);
        projectFeed.setTitle("project nice");

        TrackerFeed trackerFeed = new TrackerFeed();
        trackerFeed.setProjectId("2222222");
//        trackerFeed.setProjectFeed(projectFeed);

        List<TrackerFeed> trackerFeeds = new ArrayList<>();
        trackerFeeds.add(trackerFeed);

        when(projectFeedRepository.findOne(1L)).thenReturn(projectFeed);
        when(trackerFeedRepository.findByProjectFeed(projectFeed)).thenReturn(trackerFeeds);

        projectFeedService.delete(1L);

        verify(projectFeedRepository).findOne(1L);
        verify(trackerFeedRepository).delete(trackerFeed);
        verify(projectFeedRepository).delete(projectFeed);
    }

    @Test
    public void itDoesNothingWhenDeletingNonexistentProjectFeed() {
        when(projectFeedRepository.findOne(1L)).thenReturn(null);

        projectFeedService.delete(1L);

        verify(projectFeedRepository).findOne(1L);
        verify(trackerFeedRepository, never()).delete(anyCollection());
        verify(projectFeedRepository, never()).delete(anyCollection());
    }

    @Test
    public void itGetsTrackerFeedsAssociatedWithProjectFeed() {
        ProjectFeed projectFeed = new ProjectFeed();
        projectFeed.setId(1L);
        projectFeed.setTitle("Pointy Project");

        TrackerFeed trackerFeed = new TrackerFeed();
        trackerFeed.setId(1L);
        trackerFeed.setProjectId("123987");

        List<TrackerFeed> trackerFeeds = new ArrayList<>();
        trackerFeeds.add(trackerFeed);

        when(projectFeedRepository.findOne(1L)).thenReturn(projectFeed);
        when(trackerFeedRepository.findByProjectFeed(projectFeed)).thenReturn(trackerFeeds);

        ProjectFeed fetchedProjectFeed = projectFeedService.get(1L);

        assertThat(fetchedProjectFeed.getTrackerFeeds(), containsInAnyOrder(trackerFeed));
    }

    @Test
    public void itGetsAllProjectFeedsForCurrentUser() {
        ProjectFeed projectFeed1 = new ProjectFeed();
        projectFeed1.setId(1L);
        projectFeed1.setTitle("First Project");

        ProjectFeed projectFeed2 = new ProjectFeed();
        projectFeed2.setId(2L);
        projectFeed2.setTitle("Second Project");

        List<ProjectFeed> projectFeeds = new ArrayList<>();
        projectFeeds.add(projectFeed1);
        projectFeeds.add(projectFeed2);


        TrackerFeed trackerFeed1 = new TrackerFeed();
        trackerFeed1.setId(1L);
        trackerFeed1.setProjectId("1");

        List<TrackerFeed> trackerFeeds1 = new ArrayList<>();
        trackerFeeds1.add(trackerFeed1);

        TrackerFeed trackerFeed2 = new TrackerFeed();
        trackerFeed2.setId(2L);
        trackerFeed2.setProjectId("2");

        List<TrackerFeed> trackerFeeds2 = new ArrayList<>();
        trackerFeeds2.add(trackerFeed2);


        when(trackerFeedRepository.findByProjectFeed(projectFeed1)).thenReturn(trackerFeeds1);
        when(trackerFeedRepository.findByProjectFeed(projectFeed2)).thenReturn(trackerFeeds2);
        when(projectFeedRepository.findAllForCurrentUser()).thenReturn(projectFeeds);

        List<ProjectFeed> fetchedProjectFeeds = projectFeedService.getAllForCurrentUser();

        assertThat(fetchedProjectFeeds, containsInAnyOrder(projectFeed1, projectFeed2));

        assertThat(fetchedProjectFeeds.get(0).getId(), equalTo(fetchedProjectFeeds.get(0).getTrackerFeeds().get(0).getId()));
        assertThat(fetchedProjectFeeds.get(1).getId(), equalTo(fetchedProjectFeeds.get(1).getTrackerFeeds().get(0).getId()));
    }
}
