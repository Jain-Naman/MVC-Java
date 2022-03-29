package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.todo.Controller.ToDoController;
import com.example.todo.Model.ToDoModel;
import com.example.todo.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener, FirebaseResponseListener {

    private RecyclerView tasksRecyclerView;
    private ToDoController tasksController;
    private FloatingActionButton fab;

    private List<ToDoModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        db = new DatabaseHandler();

        taskList = new ArrayList<>();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksController = new ToDoController(db, this);
        tasksRecyclerView.setAdapter(tasksController);

        fab = findViewById(R.id.fab);

        db.getAllTasks(new FirebaseResponseListener() {
            @Override
            public void callback(List<ToDoModel> tasks) {
                taskList = tasks;
                Log.d("tasks", "IN CALLBACK " + taskList.toString());
                tasksController.setTasks(taskList);
                tasksController.notifyDataSetChanged();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTask.newInstance().show(getSupportFragmentManager(), NewTask.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        db.getAllTasks(new FirebaseResponseListener() {
            @Override
            public void callback(List<ToDoModel> tasks) {
                taskList = tasks;
                Log.d("tasks", "IN CALLBACK " + taskList.toString());
                tasksController.setTasks(taskList);
                tasksController.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void callback(List<ToDoModel> tasks) {
        taskList = tasks;
    }
}