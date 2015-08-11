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
public class GitHubFeed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "repository_url", nullable = false)
    private String repositoryURL;

    @NotNull
    @Column(name = "repository_name", nullable = false)
    private String repositoryName;

    @NotNull
    @Column(name = "repository_owner", nullable = false)
    private String repositoryOwner;

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

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryOwner() {
        return repositoryOwner;
    }

    public void setRepositoryOwner(String repositoryOwner) {
        this.repositoryOwner = repositoryOwner;
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

        GitHubFeed gitHubFeed = (GitHubFeed) o;

        if ( ! Objects.equals(id, gitHubFeed.id)) return false;

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
            ", repositoryURL='" + repositoryURL + '\'' +
            ", repositoryName='" + repositoryName + '\'' +
            ", repositoryOwner='" + repositoryOwner + '\'' +
            ", projectFeed=" + projectFeed +
            '}';
    }
}
