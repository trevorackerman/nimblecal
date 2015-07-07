package com.nimblehammer.domain.github;

public class GitHubCommit {
    private String sha;
    private String message;
    private boolean distinct;
    private String url;
    private GitHubAuthor author;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GitHubAuthor getAuthor() {
        return author;
    }

    public void setAuthor(GitHubAuthor author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GitHubCommit)) return false;

        GitHubCommit that = (GitHubCommit) o;

        if (distinct != that.distinct) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (sha != null ? !sha.equals(that.sha) : that.sha != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sha != null ? sha.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (distinct ? 1 : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GitHubCommit{" +
            "sha='" + sha + '\'' +
            ", message='" + message + '\'' +
            ", distinct=" + distinct +
            ", url='" + url + '\'' +
            ", author=" + author +
            '}';
    }
}

