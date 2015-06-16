package com.nimblehammer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackerResource {
    private String kind;
    private int id;
    private String name;
    private String story_type;
    private String url;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory_type() {
        return story_type;
    }

    public void setStory_type(String story_type) {
        this.story_type = story_type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackerResource)) return false;

        TrackerResource that = (TrackerResource) o;

        if (id != that.id) return false;
        if (!kind.equals(that.kind)) return false;
        if (!name.equals(that.name)) return false;
        if (!story_type.equals(that.story_type)) return false;
        if (!url.equals(that.url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = kind.hashCode();
        result = 31 * result + id;
        result = 31 * result + name.hashCode();
        result = 31 * result + story_type.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TrackerResource{" +
            "kind='" + kind + '\'' +
            ", id=" + id +
            ", name='" + name + '\'' +
            ", story_type='" + story_type + '\'' +
            ", url='" + url + '\'' +
            '}';
    }
}
