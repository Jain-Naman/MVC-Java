package com.example.todo.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.MainActivity;
import com.example.todo.Model.ToDoModel;
import com.example.todo.R;
import com.example.todo.Utils.DatabaseHandler;

import java.util.List;

public class ToDoController extends RecyclerView.Adapter<ToDoController.ViewHolder> {

    private List<ToDoModel> toDoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoController(DatabaseHandler db, MainActivity activity) {
        this.activity = activity;
        this.db = db;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ToDoModel item = toDoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    db.updateStatus(item.getId(), 1);
                    Toast.makeText(activity.getApplicationContext(), "Task completed", Toast.LENGTH_SHORT).show();
                }else{
                    db.updateStatus(item.getId(), 0);
                    Toast.makeText(activity.getApplicationContext(), "Task marked incomplete", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        holder.taskDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                db.deleteTask(item.getId());
//                Toast.makeText(view.getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(toDoList == null)
            return 0;
        return toDoList.size();
    }

    public void setTasks(List<ToDoModel> todoList){
        this.toDoList = todoList;
        notifyDataSetChanged();
    }

    private boolean toBoolean(int num){
        return num!=0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        Button taskDelete;
        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
            taskDelete = view.findViewById(R.id.toDoTaskDelete);
        }
    }
}

