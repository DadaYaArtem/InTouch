package com.example.intouch.api;

import com.example.intouch.model.ToDoEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyApiService {
    @GET("tasks")
    Call<List<ToDoEntity>> getAllTasks();
}
