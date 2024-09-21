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
        pickTimeButton = findViewById(R.id.pickTimeButton);
        pickTimeButton.setText(getTodaysTime());

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

    // Get today time information: hour - minute
    private String getTodaysTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour + ":" + minute;
    }

    // Time Picker for user to select their practice time
    private void initTimePicker() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog with Holo theme
        timePickerDialog = new TimePickerDialog(
                Physical.this,
                android.R.style.Theme_Holo_Light_Dialog,  // Use Holo theme here
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Display the selected time
                        pickTimeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, true); // true for 24-hour format
    }

    // open Time picker
    public void openTimePicker(View view) {
        timePickerDialog.show();
    }
}