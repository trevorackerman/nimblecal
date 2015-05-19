package com.nimblehammer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BitbucketFeed.
 */
@Entity
@Table(name = "BITBUCKETFEED")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BitbucketFeed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "site", nullable = false)
    private String site;

    @NotNull
    @Column(name = "repository_owner", nullable = false)
    private String repositoryOwner;

    @NotNull
    @Column(name = "repository_name", nullable = false)
    private String repositoryName;

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

    public String getRepositoryOwner() {
        return repositoryOwner;
    }

    public void setRepositoryOwner(String repositoryOwner) {
        this.repositoryOwner = repositoryOwner;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
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

        BitbucketFeed bitbucketFeed = (BitbucketFeed) o;

        if ( ! Objects.equals(id, bitbucketFeed.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BitbucketFeed{" +
                "id=" + id +
                ", site='" + site + "'" +
                ", repositoryOwner='" + repositoryOwner + "'" +
                ", repositoryName='" + repositoryName + "'" +
                '}';
    }
}
