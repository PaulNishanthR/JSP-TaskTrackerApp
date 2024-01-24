package com.codewithnishanth.model;

public class Task {
    private int id;
    private String taskName;
    private String description;
    private boolean completed;
    private boolean flagID;

    public boolean isFlagID() {
        return flagID;
    }

    public void setFlagID(boolean flagID) {
        this.flagID = flagID;
    }

    public Task(int id, String taskName, String description, boolean completed, boolean flagID) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.completed = completed;
        this.flagID=flagID;
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}