package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Notification extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private List<NotificationData> list_items = new ArrayList<>();
    private String name;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.recycleView);
        backButton = findViewById(R.id.back_button);

        firebaseFirestore = FirebaseFirestore.getInstance();

        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name",null);

        loadData();

        NotificationAdapter viewAdapter = new NotificationAdapter(this, list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(viewAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void loadData() {
        firebaseFirestore.collection("GoodLife")
                .document(name)
                .collection("Thông Báo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            // Loop through all documents
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                NotificationData new_item = new NotificationData(document.getString("name")
                                        , document.getString("information"), document.getString("time"));
                                list_items.add(new_item);
                                Toast.makeText(Notification.this, "" + new_item.name + " " + new_item.information + " " + new_item.time + "", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w("Firestore", "Error getting documents", task.getException());
                        }
                    }
                });
    }
}