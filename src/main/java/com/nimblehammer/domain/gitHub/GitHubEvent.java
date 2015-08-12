package com.nimblehammer.domain.gitHub;

import java.time.Instant;

public class GitHubEvent {
    private String id;
    private String type;
    private boolean isPublic;
    private Instant created_at;
    private GitHubActor actor;
    private GitHubPayload payload;
    private GitHubActor org;
    private String repositoryOwner;
    private String repositoryName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public GitHubActor getActor() {
        return actor;
    }

    public void setActor(GitHubActor actor) {
        this.actor = actor;
    }

    public GitHubPayload getPayload() {
        return payload;
    }

    public void setPayload(GitHubPayload payload) {
        this.payload = payload;
    }

    public GitHubActor getOrg() {
        return org;
    }

    public void setOrg(GitHubActor org) {
        this.org = org;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GitHubEvent)) return false;

        GitHubEvent that = (GitHubEvent) o;

        if (isPublic != that.isPublic) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (created_at != null ? !created_at.equals(that.created_at) : that.created_at != null) return false;
        if (actor != null ? !actor.equals(that.actor) : that.actor != null) return false;
        if (payload != null ? !payload.equals(that.payload) : that.payload != null) return false;
        if (org != null ? !org.equals(that.org) : that.org != null) return false;
        if (repositoryOwner != null ? !repositoryOwner.equals(that.repositoryOwner) : that.repositoryOwner != null)
            return false;
        return !(repositoryName != null ? !repositoryName.equals(that.repositoryName) : that.repositoryName != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (isPublic ? 1 : 0);
        result = 31 * result + (created_at != null ? created_at.hashCode() : 0);
        result = 31 * result + (actor != null ? actor.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (org != null ? org.hashCode() : 0);
        result = 31 * result + (repositoryOwner != null ? repositoryOwner.hashCode() : 0);
        result = 31 * result + (repositoryName != null ? repositoryName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GitHubEvent{" +
            "id='" + id + '\'' +
            ", type='" + type + '\'' +
            ", isPublic=" + isPublic +
            ", created_at=" + created_at +
            ", actor=" + actor +
            ", payload=" + payload +
            ", org=" + org +
            ", repositoryOwner='" + repositoryOwner + '\'' +
            ", repositoryName='" + repositoryName + '\'' +
            '}';
    }
}
