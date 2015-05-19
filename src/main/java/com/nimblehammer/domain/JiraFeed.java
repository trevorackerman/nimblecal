package com.nimblehammer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A JiraFeed.
 */
@Entity
@Table(name = "JIRAFEED")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JiraFeed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "site", nullable = false)
    private String site;

    @NotNull
    @Column(name = "project_id", nullable = false)
    private String projectId;

    @ManyToOne
    private ProjectFeed projectFeed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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

        JiraFeed jiraFeed = (JiraFeed) o;

        if ( ! Objects.equals(id, jiraFeed.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JiraFeed{" +
                "id=" + id +
                ", site='" + site + "'" +
                ", projectId='" + projectId + "'" +
                '}';
    }
}
