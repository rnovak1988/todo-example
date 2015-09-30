package org.rnovak;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

    Integer id = null;
    private String title = "";
    private String details = "";
    private boolean completed = false;
    private boolean archived = false;

    public Task() {

    }

    public Task(Integer id, String title, String details, boolean completed, boolean archived) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.completed = completed;
        this.archived = archived;
    }

    @JsonProperty("id")
    public Integer getId() { return this.id; }

    @JsonProperty("title")
    public String getTitle() { return this.title; }

    @JsonProperty("details")
    public String getDetails() { return this.details; }

    @JsonProperty("isCompleted")
    public boolean isCompleted() { return this.completed; }

    @JsonProperty("isArchived")
    public boolean isArchived() { return this.archived; }

    public void setId(Integer id) { this.id = id; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
