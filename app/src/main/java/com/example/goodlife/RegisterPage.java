package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterPage extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    TextInputEditText editTextName, editTextPassword, editTextGender;

    Button signUp;

    LinearLayout signIn;

    FirebaseDatabase database;
    DatabaseReference reference;

    String date, signUpDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initDatePicker();

        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
        editTextName = findViewById(R.id.acc_name);
        editTextPassword = findViewById(R.id.acc_password);
        editTextGender = findViewById(R.id.acc_gender);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        signUpDate = getTodaysDate();

        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("user");

                String name, password, gender;
                name = String.valueOf(editTextName.getText());
                password = String.valueOf(editTextPassword.getText());
                gender = String.valueOf(editTextGender.getText());

                HelperClass helperClass = new HelperClass(name, password, date, gender, signUpDate);
                reference.child(name).setValue(helperClass);

                Toast.makeText(RegisterPage.this, "Đăng kí tài khoản thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                intent.putExtra("Date", signUpDate);
                startActivity(intent);
                finish();
            }
        });

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        date = getMonthFormat(month) + "/" + day + "/" + year;
        return "Ngày sinh: " + getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}