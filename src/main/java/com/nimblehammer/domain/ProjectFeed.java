package com.nimblehammer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A ProjectFeed.
 */
@Entity
@Table(name = "PROJECTFEED")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectFeed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "projectFeed")
    private List<TrackerFeed> trackerFeeds = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<TrackerFeed> getTrackerFeeds() {
        return trackerFeeds;
    }

    public void setTrackerFeeds(List<TrackerFeed> trackerFeeds) {
        this.trackerFeeds = trackerFeeds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectFeed)) return false;

        ProjectFeed that = (ProjectFeed) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (trackerFeeds != null ? !trackerFeeds.equals(that.trackerFeeds) : that.trackerFeeds != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (trackerFeeds != null ? trackerFeeds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectFeed{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", owner=" + owner +
            ", trackerFeeds=" + trackerFeeds +
            '}';
    }
}
