package com.nimblehammer.domain.gitHub;

import java.util.List;

public class GitHubPayload {
    private int push_id;
    private int size;
    private int distinct_size;
    private String ref;
    private String head;
    private String before;
    private List<GitHubCommit> commits;

    public int getPush_id() {
        return push_id;
    }

    public void setPush_id(int push_id) {
        this.push_id = push_id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDistinct_size() {
        return distinct_size;
    }

    public void setDistinct_size(int distinct_size) {
        this.distinct_size = distinct_size;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public List<GitHubCommit> getCommits() {
        return commits;
    }

    public void setCommits(List<GitHubCommit> commits) {
        this.commits = commits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GitHubPayload)) return false;

        GitHubPayload that = (GitHubPayload) o;

        if (distinct_size != that.distinct_size) return false;
        if (push_id != that.push_id) return false;
        if (size != that.size) return false;
        if (before != null ? !before.equals(that.before) : that.before != null) return false;
        if (commits != null ? !commits.equals(that.commits) : that.commits != null) return false;
        if (head != null ? !head.equals(that.head) : that.head != null) return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = push_id;
        result = 31 * result + size;
        result = 31 * result + distinct_size;
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (head != null ? head.hashCode() : 0);
        result = 31 * result + (before != null ? before.hashCode() : 0);
        result = 31 * result + (commits != null ? commits.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GitHubPayload{" +
            "push_id=" + push_id +
            ", size=" + size +
            ", distinct_size=" + distinct_size +
            ", ref='" + ref + '\'' +
            ", head='" + head + '\'' +
            ", before='" + before + '\'' +
            ", commits=" + commits +
            '}';
    }
}
