package com.example.intouch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.intouch.adapter.ToDoAdapter;
import com.example.intouch.model.ToDoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private RecyclerView tasksRecycleView;
    private ToDoAdapter tasksAdapter;
    private List<ToDoEntity> tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        tasksList = new ArrayList<>();

        tasksRecycleView = findViewById(R.id.tasksRecycleView);
        tasksRecycleView.setLayoutManager(new LinearLayoutManager(this ));
        tasksAdapter = new ToDoAdapter(this);
        tasksRecycleView.setAdapter(tasksAdapter);

        tasksList.add(new ToDoEntity(ThreadLocalRandom.current().nextInt(0 ,10000), false,  UUID.randomUUID().toString()));
        tasksList.add(new ToDoEntity(ThreadLocalRandom.current().nextInt(0 ,10000), false,  UUID.randomUUID().toString()));
        tasksList.add(new ToDoEntity(ThreadLocalRandom.current().nextInt(0 ,10000), false,  UUID.randomUUID().toString()));
        tasksList.add(new ToDoEntity(ThreadLocalRandom.current().nextInt(0 ,10000), false,  UUID.randomUUID().toString()));
        tasksList.add(new ToDoEntity(ThreadLocalRandom.current().nextInt(0 ,10000), false,  UUID.randomUUID().toString()));
        tasksList.add(new ToDoEntity(ThreadLocalRandom.current().nextInt(0 ,10000), false,  UUID.randomUUID().toString()));
        tasksList.add(new ToDoEntity(ThreadLocalRandom.current().nextInt(0 ,10000), false,  UUID.randomUUID().toString()));

        tasksAdapter.setTasks(tasksList);
    }
}