package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    Button nutritionalStatus, physicalActivity, dietary;

    String name, signInDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        nutritionalStatus = findViewById(R.id.nutritionalStatusButton);
        physicalActivity = findViewById(R.id.physicalActivityButton);
        dietary = findViewById(R.id.dietaryButton);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            name = extras.getString("Name");
        }

        if(extras != null) {
            signInDate = extras.getString("Date");
        }

        nutritionalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, CalculateNutritionalStatus.class);
                intent.putExtra("Date", signInDate);
                intent.putExtra("Name", name);
                startActivity(intent);
                finish();
            }
        });

        physicalActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, PhysicalActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dietary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, Dietary.class);
                startActivity(intent);
                finish();
            }
        });
    }
}