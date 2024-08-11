package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class NutritionalStatusResult extends AppCompatActivity {

    Button backButton;

    String name, signInDate, gender, password, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritional_status_result);

        backButton = findViewById(R.id.back_button);

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("Name",null);
        signInDate = sharedPreferences.getString("SignInDate", null);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query userDatabase = reference.orderByChild("name").equalTo(name);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gender = snapshot.child(name).child("gender").getValue(String.class);
                password = snapshot.child(name).child("password").getValue(String.class);
                date = snapshot.child(name).child("date_of_birth").getValue(String.class);

                calculateMonthAge();
//                Toast.makeText(NutritionalStatusResult.this,signInDate + " " +  date , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionalStatusResult.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    String getNumberMonthFormat(String month) {
        if(Objects.equals(month, "JAN"))
            return "1";
        if(Objects.equals(month, "FEB"))
            return "2";
        if(Objects.equals(month, "MAR"))
            return "3";
        if(Objects.equals(month, "APR"))
            return "4";
        if(Objects.equals(month, "MAY"))
            return "5";
        if(Objects.equals(month, "JUN"))
            return "6";
        if(Objects.equals(month, "JUL"))
            return "7";
        if(Objects.equals(month, "AUG"))
            return "8";
        if(Objects.equals(month, "SEP"))
            return "9";
        if(Objects.equals(month, "OCT"))
            return "10";
        if(Objects.equals(month, "NOV"))
            return "11";
        if(Objects.equals(month, "DEC"))
            return "11";

        return "1";
    }

    void calculateMonthAge() {
        String tempSignInDate = signInDate;
        String tempDateOfBirth = date;

        String[] signIn = tempSignInDate.split("/");
        String[] birth = tempDateOfBirth.split("/");

        String signInMonth = getNumberMonthFormat(signIn[0]);
        String signInDay = signIn[1];
        String signInYear = signIn[2];

        String birthMonth = getNumberMonthFormat(birth[0]);
        String birthDay = birth[1];
        String birthYear = birth[2];

        //Toast.makeText(NutritionalStatusResult.this,signInMonth + " " +  signInDay + " " + signInYear, Toast.LENGTH_SHORT).show();
    }
}