package com.example.todo;

import com.example.todo.Model.ToDoModel;

import java.util.List;

public interface FirebaseResponseListener {
    public void callback(List<ToDoModel> tasks);
}
