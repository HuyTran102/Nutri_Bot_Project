package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.TimePickerDialog;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Physical extends AppCompatActivity {
    private TimePickerDialog timePickerDialog;
    private Button pickTimeButton;
    private Button backButton;
    private TextView activityLevel, activitiesOfLevel;
    private Dialog dialog;
    private ArrayList<String> items = new ArrayList<>(), activities = new ArrayList<>();
    private String level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        backButton = findViewById(R.id.back_button);
        pickTimeButton = findViewById(R.id.pick_time_button);
        activityLevel = findViewById(R.id.activity_level);
        activitiesOfLevel = findViewById(R.id.activities_of_level);

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
//
////        if(level == null) {
////            activities.add("null");
////        }else if(level.equals("Nhẹ")) {
//            activities.add("Câu cá đứng");
//            activities.add("Làm việc nhà");
//            activities.add("Chơi piano");
//            activities.add("Ngồi yên");
//            activities.add("Yoga");
//            activities.add("Tập thể hình nhẹ");
//            activities.add("Bơi biển, nhẹ");
//            activities.add("Đi bộ vận tốc 3 km/giờ");
////        } else if(level.equals("Vừa")) {
//            activities.add("Bơi biến, vừa");
//            activities.add("Bơi ở bể, 2 km/h");
//            activities.add("Bóng bàn ");
//            activities.add("Aerobic tốc độ chậm");
//            activities.add("Cầu lông"); activities.add("Bắn cung");
//            activities.add("Tập thể hình, vừa");
//            activities.add("Bóng rổ");
//            activities.add("Đạp xe thư giãn");
//            activities.add("Bowling");
//            activities.add("Thể dụng dụng cụ (nhẹ và vừa)");
//            activities.add("Khiêu vũ aerobic hoặc bale");
//            activities.add("Khiêu vũ hiện đại nhanh");
//            activities.add("Câu cá đi bộ và đứng");
//            activities.add("Làm vườn");
//            activities.add("Thể dục dụng cụ");
//            activities.add("Đi bộ đường dài ");
//            activities.add("Nhảy trên bạt lò xo");
//            activities.add("Đi bộ");
//            activities.add("Trượt ván");
//            activities.add("Lướt sóng");
//            activities.add("Bơi lội tốc độ vừa phải");
//            activities.add("Bóng chuyền");
//            activities.add("Đi bộ 6km/giờ");
//            activities.add("Trượt nước");
////        } else if(level.equals("Nặng")) {
//            activities.add("Aerobic tốc độ vừa");
//            activities.add("Tập thể hình nặng");
//            activities.add("Khiêu vũ tốc độ mạnh");
//            activities.add("Đạp xe 20km/giờ");
//            activities.add("Đạp xe trên 30km/giờ");
//            activities.add("Thể dục dụng cụ,mức nặng");
//            activities.add("Khuân vác");
//            activities.add("Bóng đá có thi đấu");
//            activities.add("Chạy bộ 20km/giờ");
//            activities.add("Karate/tae Kwan do");
//            activities.add("Leo núi");
//            activities.add("Trượt patin");
//            activities.add("Trượt patin nhanh");
//            activities.add("Nhảy dây chậm");
//            activities.add("Nhảy dây nhanh");
//            activities.add("Chạy bộ 10 km/giờ");
//            activities.add("Chạy bộ 16km/giờ");
//            activities.add("Chạy bộ 13 km/giờ");
//            activities.add("Chạy bộ 14 km/giờ");
//            activities.add("Chạy bộ 12 km/giờ");
//            activities.add("Chạy bộ 11 km/giờ");
//            activities.add("Đá bóng thông thường");
//            activities.add("Bơi nhanh");
//            activities.add("Bơi vừa");
//            activities.add("Bơi giải trí");
//            activities.add("Bóng chuyền thi đấu/bãi biển");
//            activities.add("Đi bộ 9 km/giờ");
//            activities.add("Đi bộ cầu thang");
//            activities.add("Chạy nước rút");
//            activities.add("Bơi biến nặng");
//            activities.add("Bơi ở bể 2.5 km/h");
//            activities.add("Bơi ở bể 3 km/h");
//            activities.add("Bơi ở bể 3.5 km/h");
//            activities.add("Bơi ở bể 4 km/h");
////        }

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
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);

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
            level = adapter.getItem(i);
            activityLevel.setText("Mức độ hoạt động " + adapter.getItem(i));
            dialog.dismiss();

            if(adapter.getItem(i) == null) {
                activities.add("null");
            }else if(level.equals("Nhẹ")) {
                activities.clear();

                activities.add("Câu cá đứng");
                activities.add("Làm việc nhà");
                activities.add("Chơi piano");
                activities.add("Ngồi yên");
                activities.add("Yoga");
                activities.add("Tập thể hình nhẹ");
                activities.add("Bơi biển, nhẹ");
                activities.add("Đi bộ vận tốc 3 km/giờ");
            } else if(adapter.getItem(i).equals("Vừa")) {
                activities.clear();

                activities.add("Bơi biến, vừa");
                activities.add("Bơi ở bể, 2 km/h");
                activities.add("Bóng bàn ");
                activities.add("Aerobic tốc độ chậm");
                activities.add("Cầu lông"); activities.add("Bắn cung");
                activities.add("Tập thể hình, vừa");
                activities.add("Bóng rổ");
                activities.add("Đạp xe thư giãn");
                activities.add("Bowling");
                activities.add("Thể dụng dụng cụ (nhẹ và vừa)");
                activities.add("Khiêu vũ aerobic hoặc bale");
                activities.add("Khiêu vũ hiện đại nhanh");
                activities.add("Câu cá đi bộ và đứng");
                activities.add("Làm vườn");
                activities.add("Thể dục dụng cụ");
                activities.add("Đi bộ đường dài ");
                activities.add("Nhảy trên bạt lò xo");
                activities.add("Đi bộ");
                activities.add("Trượt ván");
                activities.add("Lướt sóng");
                activities.add("Bơi lội tốc độ vừa phải");
                activities.add("Bóng chuyền");
                activities.add("Đi bộ 6km/giờ");
                activities.add("Trượt nước");
            } else if(adapter.getItem(i).equals("Nặng")) {
                activities.clear();

                activities.add("Aerobic tốc độ vừa");
                activities.add("Tập thể hình nặng");
                activities.add("Khiêu vũ tốc độ mạnh");
                activities.add("Đạp xe 20km/giờ");
                activities.add("Đạp xe trên 30km/giờ");
                activities.add("Thể dục dụng cụ,mức nặng");
                activities.add("Khuân vác");
                activities.add("Bóng đá có thi đấu");
                activities.add("Chạy bộ 20km/giờ");
                activities.add("Karate/tae Kwan do");
                activities.add("Leo núi");
                activities.add("Trượt patin");
                activities.add("Trượt patin nhanh");
                activities.add("Nhảy dây chậm");
                activities.add("Nhảy dây nhanh");
                activities.add("Chạy bộ 10 km/giờ");
                activities.add("Chạy bộ 16km/giờ");
                activities.add("Chạy bộ 13 km/giờ");
                activities.add("Chạy bộ 14 km/giờ");
                activities.add("Chạy bộ 12 km/giờ");
                activities.add("Chạy bộ 11 km/giờ");
                activities.add("Đá bóng thông thường");
                activities.add("Bơi nhanh");
                activities.add("Bơi vừa");
                activities.add("Bơi giải trí");
                activities.add("Bóng chuyền thi đấu/bãi biển");
                activities.add("Đi bộ 9 km/giờ");
                activities.add("Đi bộ cầu thang");
                activities.add("Chạy nước rút");
                activities.add("Bơi biến nặng");
                activities.add("Bơi ở bể 2.5 km/h");
                activities.add("Bơi ở bể 3 km/h");
                activities.add("Bơi ở bể 3.5 km/h");
                activities.add("Bơi ở bể 4 km/h");
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
}