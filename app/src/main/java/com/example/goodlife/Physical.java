package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class Physical extends AppCompatActivity {
    private TimePickerDialog timePickerDialog;
    private Button pickTimeButton;
    private Button backButton;
    private TextView activityLevel;
    private Dialog dialog;
    private ArrayList<String> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        backButton = findViewById(R.id.back_button);
        pickTimeButton = findViewById(R.id.pick_time_button);
        activityLevel = findViewById(R.id.activity_level);

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

        // Set current time for both time picker button
        pickTimeButton.setText(" Thời gian luyện tập: 0 giờ : 0 phút");

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
        dialog.getWindow().setLayout(300, 500);  // Set dialog size as per your requirement

        // Initialize dialog views
        EditText editTextSearch = dialog.findViewById(R.id.editTextSearch);
        ListView listView = dialog.findViewById(R.id.listView);

        // Set up adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
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
            activityLevel.setText(adapter.getItem(i));
            dialog.dismiss();
        });

        dialog.show();
    }
}