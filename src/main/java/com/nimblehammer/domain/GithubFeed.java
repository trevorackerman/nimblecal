package com.nimblehammer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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
    private Calendar calendar;

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

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
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
