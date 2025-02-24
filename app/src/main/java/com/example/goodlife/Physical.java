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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.TimePickerDialog;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Physical extends AppCompatActivity {
    private TimePickerDialog timePickerDialog;
    private Button pickTimeButton, backButton, activityLevel, activitiesOfLevel, addActivity;
    private Dialog dialog;
    private ArrayList<String> items = new ArrayList<>(), activities = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String name;
    private double userWeight, sumUsedEnergy;
    private TextView totalUsedEnergy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        // Make status bar fully transparent
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));

        // Set the layout to extend into the status bar
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        backButton = findViewById(R.id.back_button);
        pickTimeButton = findViewById(R.id.pick_time_button);
        activityLevel = findViewById(R.id.activity_level);
        activitiesOfLevel = findViewById(R.id.activities_of_level);
        addActivity = findViewById(R.id.add_activity);
        totalUsedEnergy = findViewById(R.id.total_used_energy);

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("Name", null);

        sumUsedEnergy = 0;

        items.add("Nhẹ");
        items.add("Vừa");
        items.add("Nặng");

        LoadDataFireBase();

        CalculateSumUsedEnergy();

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
        pickTimeButton.setText("Thời gian luyện tập 00 giờ : 00 phút");

        // use to open the dialog to select the user practice time
        initTimePicker();

        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] activityNameSplit = String.valueOf(activitiesOfLevel.getText()).split("-");
                String itemActivityName = activityNameSplit[0].substring(9);
                String itemActivityMET = activityNameSplit[1].substring(5);
                String[] activityLevelSplit = String.valueOf(activityLevel.getText()).split(" ");
                String itemActivityLevel = activityLevelSplit[4];
                String itemPracticeTime = String.valueOf(pickTimeButton.getText()).substring(20);
                int prac_hour = Integer.parseInt(itemPracticeTime.substring(0, 2));
                int prac_minute = Integer.parseInt(itemPracticeTime.substring(9, 11));
                prac_minute += (prac_hour * 60);
                itemPracticeTime = String.valueOf(prac_minute);

                // Get the current date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month += 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);

                double itemUsedEnergy = (Double.parseDouble(itemActivityMET) * userWeight * 3.5 * prac_minute) / 200;

//                Toast.makeText(Physical.this, itemUsedEnergy + " " + itemActivityMET + " " + userWeight + " " + prac_minute, Toast.LENGTH_SHORT).show();

                WriteDataFireBase(itemActivityName, itemActivityLevel, itemActivityMET
                        , itemPracticeTime, String.valueOf(itemUsedEnergy), String.valueOf(year)
                        , String.valueOf(month), String.valueOf(day)
                        , String.valueOf(hour), String.valueOf(minute), String.valueOf(second));

                CalculateSumUsedEnergy();
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
                pickTimeButton.setText(String.format("Thời gian luyện tập %02d giờ : %02d phút", hourOfDay, minute));
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

            if(adapter.getItem(i) == "") {
                activities.add(" - MET 0.0");
            }else if(adapter.getItem(i).equals("Nhẹ")) {
                activities.clear();

                activities.add("Câu cá đứng - MET 2.5");
                activities.add("Làm việc nhà - MET 2.5");
                activities.add("Chơi piano - MET 2.5");
                activities.add("Ngồi yên - MET 1.0");
                activities.add("Yoga - MET 2.5");
                activities.add("Tập thể hình nhẹ - MET 3.0");
                activities.add("Bơi biến nhẹ - MET 2.0");
                activities.add("Đi bộ vận tốc 3 km/giờ - MET 2.5");

            } else if(adapter.getItem(i).equals("Vừa")) {
                activities.clear();
                activities.add("Bơi biến vừa - MET 3.0");
                activities.add("Bơi ở bể 2 km/h - MET 4.3");
                activities.add("Bóng bàn - MET 4.7");
                activities.add("Aerobic tốc độ chậm - MET 5.0");
                activities.add("Cầu lông - MET 4.5");
                activities.add("Bắn cung - MET 3.5");
                activities.add("Tập thể hình vừa - MET 5.0");
                activities.add("Bóng rổ - MET 4.5");
                activities.add("Đạp xe thư giãn - MET 3.5");
                activities.add("Bowling - MET 3.0");
                activities.add("Thể dụng dụng cụ (nhẹ và vừa) - MET 3.5");
                activities.add("Khiêu vũ aerobic hoặc bale - MET 6.0");
                activities.add("Khiêu vũ hiện đại nhanh - MET 4.8");
                activities.add("Câu cá đi bộ và đứng - MET 3.5");
                activities.add("Làm vườn - MET 4.0");
                activities.add("Thể dục dụng cụ - MET 4.0");
                activities.add("Đi bộ đường dài - MET 6.0");
                activities.add("Nhảy trên bạt lò xo - MET 4.5");
                activities.add("Đi bộ - MET 5.5");
                activities.add("Trượt ván - MET 5.0");
                activities.add("Lướt sóng - MET 6.0");
                activities.add("Bơi lội tốc độ vừa phải - MET 4.5");
                activities.add("Bóng chuyền - MET 3.0");
                activities.add("Đi bộ 6km/giờ - MET 5.0");
                activities.add("Trượt nước - MET 6.0");
            } else if(adapter.getItem(i).equals("Nặng")) {
                activities.clear();

                activities.add("Aerobic tốc độ vừa - MET 6.5");
                activities.add("Tập thể hình nặng - MET 7.0");
                activities.add("Khiêu vũ tốc độ mạnh - MET 7.0");
                activities.add("Đạp xe 20km/giờ - MET 8.0");
                activities.add("Đạp xe trên 30km/giờ - MET 16.0");
                activities.add("Thể dục dụng cụ mức nặng - MET 8.0");
                activities.add("Khuân vác - MET 7.0");
                activities.add("Bóng đá có thi đấu - MET 9.0");
                activities.add("Chạy bộ 20km/giờ - MET 8.0");
                activities.add("Karate/tae Kwan do - MET 10.0");
                activities.add("Leo núi - MET 8.0");
                activities.add("Trượt patin - MET 7.0");
                activities.add("Trượt patin nhanh - MET 12.0");
                activities.add("Nhảy dây chậm - MET 8.0");
                activities.add("Nhảy dây nhanh - MET 12.0");
                activities.add("Chạy bộ 10 km/giờ - MET 10.0");
                activities.add("Chạy bộ 16km/giờ - MET 16.0");
                activities.add("Chạy bộ 13 km/giờ - MET 14.0");
                activities.add("Chạy bộ 14 km/giờ - MET 12.0");
                activities.add("Chạy bộ 12 km/giờ - MET 12.5");
                activities.add("Chạy bộ 11 km/giờ - MET 11.0");
                activities.add("Đá bóng thông thường - MET 7.0");
                activities.add("Bơi nhanh - MET 10.0");
                activities.add("Bơi vừa - MET 7.0");
                activities.add("Bơi giải trí - MET 6.0");
                activities.add("Bóng chuyền thi đấu/bãi biển - MET 8.0");
                activities.add("Đi bộ 9 km/giờ - MET 11.0");
                activities.add("Đi bộ cầu thang - MET 8.0");
                activities.add("Chạy nước rút - MET 8.0");
                activities.add("Bơi biến nặng - MET 6.5");
                activities.add("Bơi ở bể 2.5 km/h - MET 6.8");
                activities.add("Bơi ở bể 3 km/ - MET 8.9");
                activities.add("Bơi ở bể 3.5 km/h - MET 11.5");
                activities.add("Bơi ở bể 4 km/h - MET 13.6");
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activities);
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
            activitiesOfLevel.setText("Hoạt động " + adapter.getItem(i));
            dialog.dismiss();
        });

        dialog.show();
    }

    // Write Data to Cloud Firestone
    public void WriteDataFireBase(String userActivityName, String userActivityLevel
            , String userActivityMet, String userActivityTime, String userUsedEnergy
            , String itemAddingYear, String itemAddingMonth, String itemAddingDay
            , String itemAddingHour, String itemAddingMinute, String itemAddingSecond) {
        // Create a new item with all of the value
        Map<String, Object> item = new HashMap<>();
        item.put("userActivityName", userActivityName);
        item.put("userActivityLevel", userActivityLevel);
        item.put("userActivityMet", userActivityMet);
        item.put("userPracticeTime", userActivityTime);
        item.put("userUsedEnergy", userUsedEnergy);
        item.put("year", itemAddingYear);
        item.put("month", itemAddingMonth);
        item.put("day", itemAddingDay);
        item.put("hour", itemAddingHour);
        item.put("minute", itemAddingMinute);
        item.put("second", itemAddingSecond);

        firebaseFirestore.collection("GoodLife")
                .document(name).collection("Hoạt động thể lực")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
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

    // Load Data from database
    public void LoadDataFireBase(){
        firebaseFirestore.collection("GoodLife")
                .document(name).collection("Dinh dưỡng")
                .get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if(task.isSuccessful()) {
                        // Loop through all documents
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            if(document.getString("useHeight") != ""
                                    && document.getString("userWeight") != ""
                                    && document.getString("userRecommendHeight") != ""
                                    && document.getString("userRecommendWeight") != "") {
                                try {
                                    userWeight = Double.parseDouble(document.getString("userWeight"));
                                }catch (Exception e){
                                    userWeight = 0.0;
                                }
                            }

                        }
                    } else {
                        Log.w("Firestore", "Error getting documents", task.getException());
                    }
                });
    }

    // Load Data from database
    public void CalculateSumUsedEnergy(){
        firebaseFirestore.collection("GoodLife")
                .document(name).collection("Hoạt động thể lực")
                .get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if(task.isSuccessful()) {
                        double total_sum = 0;
                        // Get the current date
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        month += 1;
                        // Loop through all documents
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            if(document.getString("userUsedEnergy") != ""
                                    && Integer.parseInt(document.getString("day")) == day
                                    && Integer.parseInt(document.getString("month")) == month
                                    && Integer.parseInt(document.getString("year")) == year) {
                                    total_sum += Double.parseDouble(document.getString("userUsedEnergy"));
                            }
                        }

                        DecimalFormat df = new DecimalFormat("###.#");
                        totalUsedEnergy.setText(df.format(total_sum) + " Kcal");
                    } else {
                        Log.w("Firestore", "Error getting documents", task.getException());
                    }
                });
    }
}