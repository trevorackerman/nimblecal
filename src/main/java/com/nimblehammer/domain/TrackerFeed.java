package com.nimblehammer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TrackerFeed.
 */
@Entity
@Table(name = "TRACKERFEED")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TrackerFeed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "project_id", nullable = false)
    private String projectId;

    @ManyToOne
//    @JoinColumn(name="projectfeed_id")
    private ProjectFeed projectFeed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public ProjectFeed getProjectFeed() {
        return projectFeed;
    }

    public void setProjectFeed(ProjectFeed projectFeed) {
        this.projectFeed = projectFeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TrackerFeed trackerFeed = (TrackerFeed) o;

        if ( ! Objects.equals(id, trackerFeed.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TrackerFeed{" +
                "id=" + id +
                ", projectId='" + projectId + "'" +
                '}';
    }
}
