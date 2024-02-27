package com.example.intouch.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.intouch.R;
import com.example.intouch.adapter.ToDoAdapter;
import com.example.intouch.callback.SwipeToDeleteCallback;
import com.example.intouch.model.MainViewModel;
import com.example.intouch.model.ToDoEntity;
import com.example.intouch.service.TaskService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskService.TaskServiceCallback {
    private static final long NEW_TASK_ID = 0;

    private long userId;
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private List<ToDoEntity> tasksList;
    private TaskService taskService;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        this.userId = sharedPreferences.getLong("userId", 0);
        System.out.println("userId in main: " + userId);
        System.out.println("token in main: " + sharedPreferences.getString("token", ""));

        taskService = new TaskService(getApplication());
        tasksList = new ArrayList<>();

        setupRecyclerView();
        setupViewModel();
        setupFloatingActionButton();
        setupItemTouchHelper();
    }

    private void setupRecyclerView() {
        tasksRecyclerView = findViewById(R.id.tasksRecycleView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(this, taskService);
        tasksRecyclerView.setAdapter(tasksAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if (viewModel.getTasks() != null && viewModel.getTasks().size() > 0) {
            tasksList = viewModel.getTasks();
            tasksAdapter.setTasks(tasksList);
        } else {
            taskService.getTasksByUserId(userId, this);
        }
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAddTaskDialog());
    }

    private void setupItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(new SwipeToDeleteCallback.OnSwipeCallback() {
            @Override
            public void onSwipe(int position) {
                ToDoEntity deletedTask = tasksList.get(position);
                taskService.deleteTask(deletedTask.getId(), MainActivity.this);
            }
        }));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        final EditText taskEditText = dialogView.findViewById(R.id.editTextTask);

        builder.setView(dialogView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskText = taskEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(taskText)) {
                    // Create a new task and add it to the adapter
                    ToDoEntity newTask = new ToDoEntity(
                            0,  // Assuming your backend generates the ID
                            false,
                            taskText,
                            new Date()
                    );
                    tasksAdapter.addTask(newTask);

                    // Call a method to send the new task to the backend
                    taskService.addTask(newTask, userId, MainActivity.this);
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showEditTaskDialog(ToDoEntity task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        final EditText taskEditText = dialogView.findViewById(R.id.editTextTask);
        taskEditText.setText(task.getDescription());

        builder.setView(dialogView);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskText = taskEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(taskText)) {
                    // Update the task description and update it in the adapter
                    task.setDescription(taskText);
                    tasksAdapter.notifyDataSetChanged();

                    // Call a method to send the updated task to the backend
                    taskService.updateTask(task.getId(), task, MainActivity.this);
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onTasksFetched(List<ToDoEntity> tasks) {
        if (tasks != null) {
            tasksList = tasks;
            tasksAdapter.setTasks(tasksList);

            // Save data to ViewModel for configuration changes
            ViewModelProviders.of(this).get(MainViewModel.class).setTasks(tasksList);
        } else {
            onError("Failed to get tasks");
        }
    }

    @Override
    public void onTaskAdded() {
        taskService.getTasksByUserId(userId, this);
    }

    public void onTaskUpdated() {
        taskService.getTasksByUserId(userId, this);
    }


    @Override
    public void onTaskDeleted() {
        taskService.getTasksByUserId(userId, this);
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public MainViewModel getViewModel() {
        return viewModel;
    }
}