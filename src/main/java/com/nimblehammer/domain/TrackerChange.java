package com.nimblehammer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackerChange {
    private String kind;
    private String change_type;
    private int id;
    private String story_type;
    private String name;
    private Map<String, Object> new_values;
    private Map<String, Object> original_values;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getChange_type() {
        return change_type;
    }

    public void setChange_type(String change_type) {
        this.change_type = change_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStory_type() {
        return story_type;
    }

    public void setStory_type(String story_type) {
        this.story_type = story_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getNew_values() {
        return new_values;
    }

    public void setNew_values(Map<String, Object> new_values) {
        this.new_values = new_values;
    }

    public Map<String, Object> getOriginal_values() {
        return original_values;
    }

    public void setOriginal_values(Map<String, Object> original_values) {
        this.original_values = original_values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackerChange)) return false;

        TrackerChange that = (TrackerChange) o;

        if (id != that.id) return false;
        if (change_type != null ? !change_type.equals(that.change_type) : that.change_type != null) return false;
        if (kind != null ? !kind.equals(that.kind) : that.kind != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (new_values != null ? !new_values.equals(that.new_values) : that.new_values != null) return false;
        if (original_values != null ? !original_values.equals(that.original_values) : that.original_values != null)
            return false;
        if (story_type != null ? !story_type.equals(that.story_type) : that.story_type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = kind != null ? kind.hashCode() : 0;
        result = 31 * result + (change_type != null ? change_type.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (story_type != null ? story_type.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (new_values != null ? new_values.hashCode() : 0);
        result = 31 * result + (original_values != null ? original_values.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TrackerChange{" +
            "kind='" + kind + '\'' +
            ", change_type='" + change_type + '\'' +
            ", id=" + id +
            ", story_type='" + story_type + '\'' +
            ", name='" + name + '\'' +
            ", new_values=" + new_values +
            ", original_values=" + original_values +
            '}';
    }
}
