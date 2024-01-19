package com.example.intouch.api;

import com.example.intouch.model.ToDoEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MyApiService {
    @GET("tasks")
    Call<List<ToDoEntity>> getAllTasks();

    @POST("tasks")
    Call<ToDoEntity> addTask(@Body ToDoEntity task);

    @DELETE("tasks/{taskId}")
    Call<Void> deleteTask(@Path("taskId") long taskId);

    @PUT("tasks/{taskId}")
    Call<ToDoEntity> updateTask(@Path("taskId") long taskId, @Body ToDoEntity task);
}