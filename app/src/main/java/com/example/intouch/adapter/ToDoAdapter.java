package com.example.intouch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intouch.MainActivity;
import com.example.intouch.R;
import com.example.intouch.model.ToDoEntity;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoEntity> todoList;
    private MainActivity mainActivity;

    public ToDoAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
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
        holder.task.setText(item.getDescription());
        holder.task.setChecked(item.isDone());
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

        public ViewHolder(View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.todoCheckBox);
        }
    }
}
