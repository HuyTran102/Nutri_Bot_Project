package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;


public class Physical extends AppCompatActivity {
    private TimePickerDialog timePickerDialog;
    private Button pickTimeButton;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        backButton = findViewById(R.id.back_button);
        pickTimeButton = findViewById(R.id.pick_time_button);

        // Set current time for both time picker button
        pickTimeButton.setText("0 giờ : 0 phút");

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
                pickTimeButton.setText(String.format("%02d giờ : %02d phút", hourOfDay, minute));
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
}