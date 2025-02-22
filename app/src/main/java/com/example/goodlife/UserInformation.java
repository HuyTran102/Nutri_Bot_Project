package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserInformation extends AppCompatActivity {
    private String name, date, gender, email;
    private Button backButton;
    private TextView userName, userGender, userDateOfBirth, userEmail;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        // Make status bar fully transparent
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));

        // Set the layout to extend into the status bar
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        backButton = findViewById(R.id.back_button);
        userName = findViewById(R.id.user_name);
        userGender = findViewById(R.id.user_gender);
        userDateOfBirth = findViewById(R.id.user_date_of_birth);
        userEmail = findViewById(R.id.user_email);

        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name", null);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query userDatabase = reference.orderByChild("name").equalTo(name);

        userName.setText(name);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gender = snapshot.child(name).child("gender").getValue(String.class);
                date = snapshot.child(name).child("date_of_birth").getValue(String.class);
                email = snapshot.child(name).child("email").getValue(String.class);

                if(gender.equals("Nu")) gender = "Nữ";
                userGender.setText(gender);
                userDateOfBirth.setText(makeDateString(date));
                userEmail.setText(email);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInformation.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    String getNumberMonthFormat(String month) {
        if (Objects.equals(month, "JAN"))
            return "1";
        if (Objects.equals(month, "FEB"))
            return "2";
        if (Objects.equals(month, "MAR"))
            return "3";
        if (Objects.equals(month, "APR"))
            return "4";
        if (Objects.equals(month, "MAY"))
            return "5";
        if (Objects.equals(month, "JUN"))
            return "6";
        if (Objects.equals(month, "JUL"))
            return "7";
        if (Objects.equals(month, "AUG"))
            return "8";
        if (Objects.equals(month, "SEP"))
            return "9";
        if (Objects.equals(month, "OCT"))
            return "10";
        if (Objects.equals(month, "NOV"))
            return "11";
        if (Objects.equals(month, "DEC"))
            return "11";

        return "1";
    }

    private String makeDateString(String date) {
        String tempDateOfBirth = date;
        String[] birth = tempDateOfBirth.split("/");

        int month = Integer.parseInt(getNumberMonthFormat(birth[0]));
        int day = Integer.parseInt(birth[1]);
        int year = Integer.parseInt(birth[2]);
        return month + "/" + day + "/" + year;
    }
}