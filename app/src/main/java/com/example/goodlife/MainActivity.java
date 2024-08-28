package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextInputEditText editTextName, editTextPassword;

    Button signIn;

    LinearLayout signUp;

    String signInDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.acc_name);
        editTextPassword = findViewById(R.id.acc_password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        signInDate = getTodaysDate();

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);


        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterPage.class);
            startActivity(intent);
            finish();
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validDataUsername() || !validDataUserPassword()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Name", String.valueOf(editTextName.getText()));
                    editor.putString("SignInDate", signInDate);
                    editor.apply();
                    checkUserData();
                }
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

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + "/" + day + "/" + year;
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

    public Boolean validDataUsername() {
        String name;
        name = String.valueOf(editTextName.getText());
        if(name.isEmpty()) {
            editTextName.setError("Vui lòng nhập vào tên người dùng!");
            return false;
        } else {
            editTextName.setError(null);
            return true;
        }
    }

    public Boolean validDataUserPassword() {
        String password;
        password = String.valueOf(editTextPassword.getText());
        if(password.isEmpty()) {
            editTextName.setError("Vui lòng nhập vào mật khẩu người dùng!");
            return false;
        } else {
            editTextName.setError(null);
            return true;
        }
    }
    public void checkUserData() {
        String name, password;
        name = String.valueOf(editTextName.getText()).trim();
        password = String.valueOf(editTextPassword.getText()).trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(name);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    editTextName.setError(null);
                    String passwordFromDB = snapshot.child(name).child("password").getValue(String.class);

                    if(Objects.equals(passwordFromDB, password)) {
                        editTextName.setError(null);
                        Toast.makeText(MainActivity.this, "Đăng nhập tài khoản thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        editTextPassword.setError("Mật khẩu không đúng!");
                        editTextPassword.requestFocus();
                    }
                } else {
                    editTextName.setError("Tài khoản không tồn tại!");
                    editTextName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}