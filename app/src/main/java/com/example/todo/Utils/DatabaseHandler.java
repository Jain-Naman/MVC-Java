package com.example.todo.Utils;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todo.FirebaseResponseListener;
import com.example.todo.Model.ToDoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHandler {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void insertTask(ToDoModel task) {
        Map<String, Object> taskMap = new HashMap<>();

        taskMap.put("task", task.getTask());
        taskMap.put("status", task.getStatus());

        firestore.collection("tasks").add(taskMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("firebase", "Document written with id " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("firebase", "Error adding document", e);
            }
        });
    }

    /*public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        firestore.collection("tasks").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d: list){
                        ToDoModel task = new ToDoModel();
                        task.setTask(d.get("task").toString());
                        task.setStatus(((Long) d.get("status")).intValue());
                        task.setId(d.getId());
                        taskList.add(task);
                        Log.d("firebase","Fetched Data " + task.toString());
                    }
                }else{
                    Log.d("firebase", "No data found in Database!");
                }
                Log.d("tasks",taskList.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("firebase","Error getting data", e);
            }
        });
        Log.d("tasks", taskList.toString());
        return taskList;
    }*/

    public void getAllTasks(FirebaseResponseListener firebaseResponseListener) {
        List<ToDoModel> taskList = new ArrayList<>();
        firestore.collection("tasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        List<DocumentSnapshot> list = querySnapshot.getDocuments();
                        for (DocumentSnapshot d : list) {
                            ToDoModel mtask = new ToDoModel();
                            mtask.setTask(d.get("task").toString());
                            mtask.setStatus(((Long) d.get("status")).intValue());
                            mtask.setId(d.getId());
                            taskList.add(mtask);
                            firebaseResponseListener.callback(taskList);
                            Log.d("firebase", "Fetched Data " + task.toString());
                        }
                    } else {
                        Log.d("firebase", "No data found in Database!");
                    }
                }
            }
        });
        Log.d("tasks", "Database taskList " +taskList.toString());
    }

    public void updateStatus(String id, int status) {
        firestore.collection("tasks").document(id).update("status",status);
    }

    public void deleteTask(String id) {
        firestore.collection("tasks").document(id).delete();
    }
}
