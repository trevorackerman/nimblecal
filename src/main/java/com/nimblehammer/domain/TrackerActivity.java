package com.nimblehammer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackerActivity {
    private String kind;
    private String message;
    private Instant occurred_at;
    private TrackerPerformer performed_by;
    private TrackerChange[] changes;
    private TrackerResource[] primary_resources;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getOccurred_at() {
        return occurred_at;
    }

    public void setOccurred_at(Instant occurred_at) {
        this.occurred_at = occurred_at;
    }

    public TrackerPerformer getPerformed_by() {
        return performed_by;
    }

    public void setPerformed_by(TrackerPerformer performed_by) {
        this.performed_by = performed_by;
    }

    public TrackerChange[] getChanges() {
        return changes;
    }

    public void setChanges(TrackerChange[] changes) {
        this.changes = changes;
    }

    public TrackerResource[] getPrimary_resources() {
        return primary_resources;
    }

    public void setPrimary_resources(TrackerResource[] primary_resources) {
        this.primary_resources = primary_resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackerActivity)) return false;

        TrackerActivity activity = (TrackerActivity) o;

        if (!Arrays.equals(changes, activity.changes)) return false;
        if (kind != null ? !kind.equals(activity.kind) : activity.kind != null) return false;
        if (message != null ? !message.equals(activity.message) : activity.message != null) return false;
        if (occurred_at != null ? !occurred_at.equals(activity.occurred_at) : activity.occurred_at != null) return false;
        if (performed_by != null ? !performed_by.equals(activity.performed_by) : activity.performed_by != null)
            return false;
        if (!Arrays.equals(primary_resources, activity.primary_resources)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = kind != null ? kind.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (occurred_at != null ? occurred_at.hashCode() : 0);
        result = 31 * result + (performed_by != null ? performed_by.hashCode() : 0);
        result = 31 * result + (changes != null ? Arrays.hashCode(changes) : 0);
        result = 31 * result + (primary_resources != null ? Arrays.hashCode(primary_resources) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TrackerActivity{" +
            "kind='" + kind + '\'' +
            ", message='" + message + '\'' +
            ", occurred_at=" + occurred_at +
            ", performed_by=" + performed_by +
            ", changes=" + Arrays.toString(changes) +
            ", primary_resources=" + Arrays.toString(primary_resources) +
            '}';
    }
}

