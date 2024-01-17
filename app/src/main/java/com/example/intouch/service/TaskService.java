package com.example.intouch.service;

import com.example.intouch.api.TaskApiClient;
import com.example.intouch.model.ToDoEntity;

import java.util.List;
import java.util.concurrent.Executors;

public class TaskService {
    private TaskApiClient taskApiClient = new TaskApiClient();

    public interface TaskServiceCallback {
        void onTasksFetched(List<ToDoEntity> tasks);
        void onTaskAdded();
        void onTaskDeleted();
        void onError(String errorMessage);
    }

    public void fetchTasks(TaskServiceCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            taskApiClient.getAllTasks(new TaskApiClient.TaskApiCallback() {
                @Override
                public void onTaskApiResponse(Object response) {
                    callback.onTasksFetched((List<ToDoEntity>) response);
                }

                @Override
                public void onTaskApiError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        });
    }

    public void addTask(ToDoEntity newTask, TaskServiceCallback callback) {
        taskApiClient.addTask(newTask, new TaskApiClient.TaskApiCallback() {
            @Override
            public void onTaskApiResponse(Object response) {
                callback.onTaskAdded();
            }

            @Override
            public void onTaskApiError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void deleteTask(long taskId, TaskServiceCallback callback) {
        taskApiClient.deleteTask(taskId, new TaskApiClient.TaskApiCallback() {
            @Override
            public void onTaskApiResponse(Object response) {
                callback.onTaskDeleted();
            }

            @Override
            public void onTaskApiError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
}