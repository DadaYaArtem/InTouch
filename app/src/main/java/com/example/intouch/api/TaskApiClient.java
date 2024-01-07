package com.example.intouch.api;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.example.intouch.model.ToDoEntity;

import java.io.IOException;
import java.util.List;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskApiClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private OkHttpClient client = new OkHttpClient();
    private MyApiService apiService;

    public TaskApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(MyApiService.class);
    }


    public interface TaskApiCallback {
        void onTaskApiResponse(List<ToDoEntity> tasks);
        void onTaskApiError(String errorMessage);
    }


    public void getAllTasks(TaskApiCallback callback) {
        Call<List<ToDoEntity>> call = apiService.getAllTasks();
        call.enqueue(new Callback<List<ToDoEntity>>() {
            @Override
            public void onResponse(Call<List<ToDoEntity>> call, Response<List<ToDoEntity>> response) {
                if (response.isSuccessful()) {
                    callback.onTaskApiResponse(response.body());
                } else {
                    callback.onTaskApiError("Failed to get tasks. Server returned " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ToDoEntity>> call, Throwable t) {
                callback.onTaskApiError("Network error: " + t.getMessage());
            }
        });
    }

}
