package com.example.intouch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intouch.activity.MainActivity;
import com.example.intouch.R;
import com.example.intouch.model.ToDoEntity;
import com.example.intouch.service.TaskService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoEntity> todoList;
    private MainActivity mainActivity;
    private TaskService taskService;

    public ToDoAdapter(MainActivity mainActivity, TaskService taskService) {
        this.mainActivity = mainActivity;
        this.taskService = taskService;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ToDoEntity item = todoList.get(position);

            System.out.println("item in onBindViewHolder: " + item);
            System.out.println("position in onBindViewHolder: " + position);

            holder.task.setOnCheckedChangeListener(null);

            holder.task.setText(item.getDescription());
            holder.task.setChecked(item.isDone());
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.showEditTaskDialog(todoList.get(position));
                }
            });


            holder.task.setOnCheckedChangeListener((buttonView, isChecked) -> {

                    item.setDone(isChecked);
                    todoList.get(position).setDone(isChecked);

                    // Update the task in the MainViewModel
                    mainActivity.getViewModel().updateTask(item);
                    // Update the task on the server
                    taskService.updateTask(item.getId(), item, new TaskService.TaskServiceCallback() {

                        @Override
                        public void onTasksFetched(List<ToDoEntity> tasks) {

                        }

                        @Override
                        public void onTaskAdded() {

                        }

                        @Override
                        public void onTaskDeleted() {

                        }

                        @Override
                        public void onError(String errorMessage) {

                        }

                        @Override
                        public void onTaskUpdated() {

                        }
                    });
            });

    }

    @Override
    public int getItemCount() {
        return todoList != null ? todoList.size() : 0;
    }

    public void setTasks(List<ToDoEntity> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void addTask(ToDoEntity task) {
        todoList.add(task);
        notifyItemInserted(todoList.size() - 1);
    }

    public void deleteTask(int position) {
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        Button editButton;

        public ViewHolder(View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.todoCheckBox);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
