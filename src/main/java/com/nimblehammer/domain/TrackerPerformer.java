package com.nimblehammer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackerPerformer {
    private int id;
    private String kind;
    private String name;
    private String initials;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackerPerformer)) return false;

        TrackerPerformer that = (TrackerPerformer) o;

        if (id != that.id) return false;
        if (!initials.equals(that.initials)) return false;
        if (!kind.equals(that.kind)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + kind.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + initials.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TrackerPerformer{" +
            "id=" + id +
            ", kind='" + kind + '\'' +
            ", name='" + name + '\'' +
            ", initials='" + initials + '\'' +
            '}';
    }
}
