package com.example.intouch.model;

public class ToDoEntity {
    private long id;
    private boolean status;
    private String task;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public ToDoEntity(long id, boolean status, String task) {
        this.id = id;
        this.status = status;
        this.task = task;
    }

    public ToDoEntity() {
    }
}
