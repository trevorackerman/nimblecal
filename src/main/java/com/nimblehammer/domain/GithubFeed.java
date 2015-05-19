package com.nimblehammer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A GithubFeed.
 */
@Entity
@Table(name = "GITHUBFEED")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GithubFeed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "repository_url", nullable = false)
    private String repositoryURL;

    @ManyToOne
    private ProjectFeed projectFeed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRepositoryURL() {
        return repositoryURL;
    }

    public void setRepositoryURL(String repositoryURL) {
        this.repositoryURL = repositoryURL;
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

        GithubFeed githubFeed = (GithubFeed) o;

        if ( ! Objects.equals(id, githubFeed.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GithubFeed{" +
                "id=" + id +
                ", repositoryURL='" + repositoryURL + "'" +
                '}';
    }
}
