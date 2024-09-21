package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.TimePickerDialog;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;


public class Physical extends AppCompatActivity {
    private TextView timeTextView;
    private Button pickTimeButton;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        backButton = findViewById(R.id.back_button);
        pickTimeButton = findViewById(R.id.pickTimeButton);
        timeTextView = findViewById(R.id.timeTextView);

        pickTimeButton.setOnClickListener(v -> {
            // Get the current time
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Create a TimePickerDialog with Holo theme
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    Physical.this,
                    android.R.style.Theme_Holo_Light_Dialog,  // Use Holo theme here
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // Display the selected time
                            timeTextView.setText(String.format("%02d:%02d", hourOfDay, minute));
                        }
                    }, hour, minute, true); // true for 24-hour format
            timePickerDialog.show();
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
}