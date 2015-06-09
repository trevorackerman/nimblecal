package com.nimblehammer.service;

import com.nimblehammer.domain.ProjectFeed;
import com.nimblehammer.domain.TrackerFeed;
import com.nimblehammer.repository.ProjectFeedRepository;
import com.nimblehammer.repository.TrackerFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjectFeedService {

    @Autowired
    private ProjectFeedRepository projectFeedRepository;

    @Autowired
    private TrackerFeedRepository trackerFeedRepository;

    public ProjectFeed save(ProjectFeed projectFeed) {
        if (projectFeed.getOwner() == null) {
            throw new IllegalArgumentException("ProjectFeed does not have a User");
        }
        projectFeedRepository.save(projectFeed);
        for (TrackerFeed trackerFeed : projectFeed.getTrackerFeeds()) {
            trackerFeedRepository.save(trackerFeed);
        }

        return projectFeed;
    }

    public void update(ProjectFeed projectFeed) {
        List<TrackerFeed> existingTrackerFeeds = trackerFeedRepository.findByProjectFeed(projectFeed);

        for (TrackerFeed existingTrackerFeed : existingTrackerFeeds) {
            if (!projectFeed.getTrackerFeeds().contains(existingTrackerFeed)) {
                trackerFeedRepository.delete(existingTrackerFeed);
            }
        }

        for (TrackerFeed newTrackerFeed : projectFeed.getTrackerFeeds()) {
            if (!existingTrackerFeeds.contains(newTrackerFeed)) {
                trackerFeedRepository.save(newTrackerFeed);
            }
        }
        projectFeedRepository.save(projectFeed);
    }

    public void delete(ProjectFeed projectFeed) {
        for (TrackerFeed trackerFeed : trackerFeedRepository.findByProjectFeed(projectFeed)) {
                trackerFeedRepository.delete(trackerFeed);
        }

        projectFeedRepository.delete(projectFeed);
    }

    public void delete(Long id) {
        ProjectFeed projectFeed = projectFeedRepository.findOne(id);

        if (projectFeed == null) {
            return;
        }

        delete(projectFeed);
    }

    public ProjectFeed get(long id) {
        ProjectFeed projectFeed = projectFeedRepository.findOne(id);

        if (projectFeed == null) {
            return projectFeed;
        }

        List<TrackerFeed> trackerFeeds = trackerFeedRepository.findByProjectFeed(projectFeed);
        projectFeed.setTrackerFeeds(trackerFeeds);

        return projectFeed;
    }

    public List<ProjectFeed> getAllForCurrentUser() {
        List<ProjectFeed> projectFeeds =  projectFeedRepository.findAllForCurrentUser();

        for (ProjectFeed projectFeed : projectFeeds) {
            projectFeed.setTrackerFeeds(trackerFeedRepository.findByProjectFeed(projectFeed));
        }

        return projectFeeds;
    }

}
