package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.TimePickerDialog;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Physical extends AppCompatActivity {
    private TimePickerDialog timePickerDialog;
    private Button pickTimeButton, backButton, activityLevel, activitiesOfLevel, addActivity;
    private Dialog dialog;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayList<PairItem> activities = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        backButton = findViewById(R.id.back_button);
        pickTimeButton = findViewById(R.id.pick_time_button);
        activityLevel = findViewById(R.id.activity_level);
        activitiesOfLevel = findViewById(R.id.activities_of_level);
        addActivity = findViewById(R.id.add_activity);

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("Name", null);

        items.add("Nhẹ");
        items.add("Vừa");
        items.add("Nặng");

        // Set OnClickListener to show the dialog on clicking the TextView
        activityLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchableSpinnerDialog();
            }
        });

        // Set OnClickListener to show the dialog on clicking the TextView
        activitiesOfLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivitiesSearchableSpinnerDialog();
            }
        });

        // Set current time for both time picker button
        pickTimeButton.setText(" Thời gian luyện tập 0 giờ : 0 phút");

        // use to open the dialog to select the user practice time
        initTimePicker();

        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteDataFireBase(String.valueOf(activitiesOfLevel.getText()), String.valueOf(activityLevel.getText()), "a", String.valueOf(pickTimeButton.getText()));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Physical.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Time Picker for user to select their practice time
    private void initTimePicker() {
        // Create a DatePickerDialog with Holo theme
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Display the selected time
                pickTimeButton.setText(String.format(" Thời gian luyện tập: %02d giờ : %02d phút", hourOfDay, minute));
            }
        };

//        // Get the current time

        // Use Holo theme here
        int style = AlertDialog.THEME_HOLO_LIGHT;

        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, 0, 0, true);
    }

    // open Time picker
    public void openTimePicker(View view) {
        timePickerDialog.show();
    }

    private void showSearchableSpinnerDialog() {
        // Create the dialog
        dialog = new Dialog(Physical.this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);

        // Initialize dialog views
        EditText editTextSearch = dialog.findViewById(R.id.editTextSearch);
        ListView listView = dialog.findViewById(R.id.listView);

        // Set up adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // Set up search functionality
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Set item click listener
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            activityLevel.setText("Mức độ hoạt động " + adapter.getItem(i));
            dialog.dismiss();

            if(adapter.getItem(i) == null) {
                activities.add(new PairItem("null", 0.0));
            }else if(adapter.getItem(i).equals("Nhẹ")) {
                activities.clear();

                activities.add(new PairItem("Câu cá đứng", 2.5));
                activities.add(new PairItem("Làm việc nhà", 2.5));
                activities.add(new PairItem("Chơi piano", 2.5));
                activities.add(new PairItem("Ngồi yên", 1.0));
                activities.add(new PairItem("Yoga", 2.5));
                activities.add(new PairItem("Tập thể hình nhẹ", 3.0));
                activities.add(new PairItem("Bơi biển, nhẹ", 2.5));
                activities.add(new PairItem("Đi bộ vận tốc 3 km/giờ", 2.5));
            } else if(adapter.getItem(i).equals("Vừa")) {
                activities.clear();

                activities.add(new PairItem("Bơi biến, vừa", 3.0));
                activities.add(new PairItem("Bơi ở bể 2 km/h", 4.3));
                activities.add(new PairItem("Bóng bàn", 4.7));
                activities.add(new PairItem("Aerobic tốc độ chậm", 5.0));
                activities.add(new PairItem("Cầu lông", 4.5));
                activities.add(new PairItem("Bắn cung", 3.5));
                activities.add(new PairItem("Tập thể hình vừa", 5.0));
                activities.add(new PairItem("Bóng rổ", 4.5));
                activities.add(new PairItem("Đạp xe thư giãn", 3.5));
                activities.add(new PairItem("Bowling", 3.0));
                activities.add(new PairItem("Thể dụng dụng cụ (nhẹ và vừa)", 3.5));
                activities.add(new PairItem("Khiêu vũ aerobic hoặc bale", 6.0));
                activities.add(new PairItem("Khiêu vũ hiện đại nhanh", 4.8));
                activities.add(new PairItem("Câu cá đi bộ và đứng", 3.5));
                activities.add(new PairItem("Làm vườn", 4.0));
                activities.add(new PairItem("Thể dục dụng cụ", 4.0));
                activities.add(new PairItem("Đi bộ đường dài ", 6.0));
                activities.add(new PairItem("Nhảy trên bạt lò xo", 4.5));
                activities.add(new PairItem("Đi bộ", 5.5));
                activities.add(new PairItem("Trượt ván", 5.0));
                activities.add(new PairItem("Lướt sóng", 6.0));
                activities.add(new PairItem("Bơi lội tốc độ vừa phải", 4.5));
                activities.add(new PairItem("Bóng chuyền", 3.0));
                activities.add(new PairItem("Đi bộ 6km/giờ", 5.0));
                activities.add(new PairItem("Trượt nước", 6.0));
            } else if(adapter.getItem(i).equals("Nặng")) {
                activities.clear();

                activities.add(new PairItem("Aerobic tốc độ vừa", 6.5));
                activities.add(new PairItem("Tập thể hình nặng", 7.0));
                activities.add(new PairItem("Khiêu vũ tốc độ mạnh", 7.0));
                activities.add(new PairItem("Đạp xe 20km/giờ", 8.0));
                activities.add(new PairItem("Đạp xe trên 30km/giờ", 16.0));
                activities.add(new PairItem("Thể dục dụng cụ mức nặng", 8.0));
                activities.add(new PairItem("Khuân vác", 7.0));
                activities.add(new PairItem("Bóng đá có thi đấu", 9.0));
                activities.add(new PairItem("Chạy bộ 20km/giờ", 8.0));
                activities.add(new PairItem("Karate/tae Kwan do", 10.0));
                activities.add(new PairItem("Leo núi", 8.0));
                activities.add(new PairItem("Trượt patin", 7.0));
                activities.add(new PairItem("Trượt patin nhanh", 12.0));
                activities.add(new PairItem("Nhảy dây chậm", 8.0));
                activities.add(new PairItem("Nhảy dây nhanh", 12.0));
                activities.add(new PairItem("Chạy bộ 10 km/giờ", 10.0));
                activities.add(new PairItem("Chạy bộ 16km/giờ", 16.0));
                activities.add(new PairItem("Chạy bộ 13 km/giờ", 14.0));
                activities.add(new PairItem("Chạy bộ 14 km/giờ", 12.0));
                activities.add(new PairItem("Chạy bộ 12 km/giờ", 12.5));
                activities.add(new PairItem("Chạy bộ 11 km/giờ", 11.0));
                activities.add(new PairItem("Đá bóng thông thường", 7.0));
                activities.add(new PairItem("Bơi nhanh", 10.0));
                activities.add(new PairItem("Bơi vừa", 7.0));
                activities.add(new PairItem("Bơi giải trí", 6.0));
                activities.add(new PairItem("Bóng chuyền thi đấu/bãi biển", 8.0));
                activities.add(new PairItem("Đi bộ 9 km/giờ", 11.0));
                activities.add(new PairItem("Đi bộ cầu thang", 8.0));
                activities.add(new PairItem("Chạy nước rút", 8.0));
                activities.add(new PairItem("Bơi biến nặng", 6.5));
                activities.add(new PairItem("Bơi ở bể 2.5 km/h", 6.8));
                activities.add(new PairItem("Bơi ở bể 3 km/h", 8.9));
                activities.add(new PairItem("Bơi ở bể 3.5 km/h", 11.5));
                activities.add(new PairItem("Bơi ở bể 4 km/h", 13.6));
            }
        });

        dialog.show();
    }

    private void showActivitiesSearchableSpinnerDialog() {
        // Create the dialog
        dialog = new Dialog(Physical.this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);

        // Initialize dialog views
        EditText editTextSearch = dialog.findViewById(R.id.editTextSearch);
        ListView listView = dialog.findViewById(R.id.listView);

        // Set up adapter
        ArrayAdapter<PairItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activities);
        listView.setAdapter(adapter);

        // Set up search functionality
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Set item click listener
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            PairItem selectedItem = adapter.getItem(i);
            activitiesOfLevel.setText("Hoạt động " + selectedItem.getKey());
            dialog.dismiss();
        });

        dialog.show();
    }

    // Write Data to Cloud Firestone
    public void WriteDataFireBase(String userActivityName, String userActivityLevel,String userActivityMet, String userActivityTime){
        // Create a new item with all of the value
        Map<String, Object> item = new HashMap<>();
        item.put("userActivityLevel", userActivityLevel);
        item.put("userActivityMet", userActivityMet);
        item.put("userPracticeTime", userActivityTime);

        firebaseFirestore.collection("GoodLife")
                .document(name).collection("Hoạt động thể lực")
                .document(userActivityName)
                .set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Adding value to database successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error adding value to database: ", e);
                    }
                });
    }
}