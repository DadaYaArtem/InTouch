package com.example.intouch.api;

import android.content.Context;


import com.example.intouch.model.ToDoEntity;

import java.util.List;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskApiClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private OkHttpClient client;
    private MyApiService apiService;
    private Context context;

    public TaskApiClient(Context context) {
        this.context = context;

        client = new OkHttpClient().newBuilder()
                .addInterceptor(new TokenInterceptor(this.context))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(MyApiService.class);
    }


    public interface TaskApiCallback {
        void onTaskApiResponse(Object response);
        void onTaskApiError(String errorMessage);
    }


    public void getAllTasks(TaskApiCallback callback) {
        Call<List<ToDoEntity>> call = apiService.getAllTasks();
        call.enqueue(new Callback<List<ToDoEntity>>() {
            @Override
            public void onResponse(Call<List<ToDoEntity>> call, Response<List<ToDoEntity>> response) {
                if (response.isSuccessful()) {
                    callback.onTaskApiResponse(response.body());
                    System.out.println("response code: taskapi " + response.code());
                    System.out.println("response body: " + response.body());
                } else {
                    callback.onTaskApiError("Failed to get tasks. Server returned " + response.code());
                    System.out.println("response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ToDoEntity>> call, Throwable t) {
                t.printStackTrace();
                callback.onTaskApiError("Network error: " + t.getMessage());
            }
        });
    }

    public void getTasksByUserId(long userId, TaskApiCallback callback) {
        Call<List<ToDoEntity>> call = apiService.getTasksByUserId(userId);
        call.enqueue(new Callback<List<ToDoEntity>>() {
            @Override
            public void onResponse(Call<List<ToDoEntity>> call, Response<List<ToDoEntity>> response) {
                if (response.isSuccessful()) {
                    callback.onTaskApiResponse(response.body());
                } else {
                    callback.onTaskApiError("getTasksByUserId: Failed to get tasks for user. Server returned " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ToDoEntity>> call, Throwable t) {
                callback.onTaskApiError("Network error: " + t.getMessage());
            }
        });
    }

    public void addTask(ToDoEntity task, TaskApiCallback callback) {
        // Assuming you have a specific endpoint for adding tasks in YourApiService
        Call<ToDoEntity> call = apiService.addTask(task);

        call.enqueue(new Callback<ToDoEntity>() {
            @Override
            public void onResponse(Call<ToDoEntity> call, Response<ToDoEntity> response) {
                if (response.isSuccessful()) {
                    System.out.println("response code taskapi: " + response.code() );
                    // Notify the callback that the task was successfully added
                    callback.onTaskApiResponse("Task added successfully");
                } else {
                    // Notify the callback about the error

                    callback.onTaskApiError("Failed to add task. Server returned " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ToDoEntity> call, Throwable t) {
                t.printStackTrace();
                // Notify the callback about the network error
                callback.onTaskApiError("Network error: " + t.getMessage());
            }
        });
    }

    public void updateTask(long taskId, ToDoEntity updatedTask, TaskApiCallback callback) {
        System.out.println("spi client updatedTask: " + updatedTask);
        // Assuming you have a Retrofit instance and a Retrofit service
        Call<ToDoEntity> call = apiService.updateTask(taskId, updatedTask);
        call.enqueue(new Callback<ToDoEntity>() {
            @Override
            public void onResponse(Call<ToDoEntity> call, Response<ToDoEntity> response) {
                if (response.isSuccessful()) {
                    callback.onTaskApiResponse(response.body());
                } else {
                    callback.onTaskApiError("Error updating task: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ToDoEntity> call, Throwable t) {
                callback.onTaskApiError("Error updating task: " + t.getMessage());
            }
        });
    }

    public void deleteTask(long taskId, TaskApiCallback callback) {
        Call<Void> call = apiService.deleteTask(taskId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onTaskApiResponse("Task deleted successfully");
                } else {
                    callback.onTaskApiError("Failed to delete task. Server returned " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onTaskApiError("Network error: " + t.getMessage());
            }
        });
    }

}
