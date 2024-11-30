package com.example.goodlife;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "scheduled_channel";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String name;

    @Override
    public void onReceive(Context context, Intent intent) {
        int value = intent.getIntExtra("EXTRA_VALUE", 0);
        int id = intent.getIntExtra("EXTRA_ID", 0);

        SharedPreferences sp = context.getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name",null);

        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.iconh)
                .setContentTitle("Bổ sung nước !")
                .setContentText("Bạn cần bổ xung thêm " + value + " ml nước")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        WriteDataFireBase("Bổ sung nước !", "Bạn cần bổ xung thêm " + value + " ml nước", "9:00");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(id, builder.build());
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Bổ xung nước";
            String description = "...";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    // Write Data to Cloud Firestone
    public void WriteDataFireBase(String titleName, String information, String time){

        // Create a new item with all of the data like name, amount, ...
        Map<String, Object> item = new HashMap<>();
        item.put("name", titleName);
        item.put("information", information);
        item.put("time", time);

        firebaseFirestore.collection("GoodLife").document(name).collection("Thông Báo")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Firestore", "Adding item to database successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error adding item to database: ", e);
                    }
                });
    }
}