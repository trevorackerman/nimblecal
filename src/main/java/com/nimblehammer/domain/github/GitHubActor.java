package com.nimblehammer.domain.github;

public class GitHubActor {
    int id;
    String login;
    String gravatar_id;
    String url;
    String avatar_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getGravatar_id() {
        return gravatar_id;
    }

    public void setGravatar_id(String gravatar_id) {
        this.gravatar_id = gravatar_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GitHubActor)) return false;

        GitHubActor that = (GitHubActor) o;

        if (id != that.id) return false;
        if (avatar_url != null ? !avatar_url.equals(that.avatar_url) : that.avatar_url != null) return false;
        if (gravatar_id != null ? !gravatar_id.equals(that.gravatar_id) : that.gravatar_id != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (gravatar_id != null ? gravatar_id.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (avatar_url != null ? avatar_url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GitHubActor{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", gravatar_id='" + gravatar_id + '\'' +
            ", url='" + url + '\'' +
            ", avatar_url='" + avatar_url + '\'' +
            '}';
    }
}
