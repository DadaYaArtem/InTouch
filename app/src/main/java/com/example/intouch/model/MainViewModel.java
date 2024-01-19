package com.example.intouch.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import java.util.List;

public class MainViewModel extends AndroidViewModel  {
    private List<ToDoEntity> tasks;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public List<ToDoEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<ToDoEntity> tasks) {
        this.tasks = tasks;
    }

    public void updateTask(ToDoEntity updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updatedTask.getId()) {
                tasks.set(i, updatedTask);
                break;
            }
        }
    }
}
