package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    Button nutritionalStatus, physicalActivity, dietary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        nutritionalStatus.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, CalculateNutritionalStatus.class);
            startActivity(intent);
            finish();
        });

        physicalActivity.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, PhysicalActivity.class);
            startActivity(intent);
            finish();
        });

        dietary.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, Dietary.class);
            startActivity(intent);
            finish();
        });
    }
}