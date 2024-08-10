package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class NutritionalStatusResult extends AppCompatActivity {

    Button backButton;

    String name, signInDate, gender, password, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritional_status_result);

        backButton = findViewById(R.id.back_button);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            name = extras.getString("Name");
        }

        if(extras != null) {
            signInDate = extras.getString("Date");
        }



        Toast.makeText(NutritionalStatusResult.this, name + " " + signInDate + " " + gender + " " + password + " " + date, Toast.LENGTH_SHORT).show();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionalStatusResult.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}