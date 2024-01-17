package com.codewithnishanth.model;

public class Task {
    private int id;
    private String taskName;
    private String description;
    private boolean completed;

    private boolean showActions;

    public boolean isShowActions() {
        return showActions;
    }

    public void setShowActions(boolean showActions) {
        this.showActions = showActions;
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
