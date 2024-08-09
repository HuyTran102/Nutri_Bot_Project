package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CalculateNutritionalStatus extends AppCompatActivity {

    Button backButton, resultButton;

    String name, signInDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_nutritional_status);

        backButton = findViewById(R.id.back_button);
        resultButton = findViewById(R.id.result_button);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            name = extras.getString("Name");
        }

        if(extras != null) {
            signInDate = extras.getString("Date");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalculateNutritionalStatus.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });

        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalculateNutritionalStatus.this, NutritionalStatusResult.class);
                intent.putExtra("Date", signInDate);
                intent.putExtra("Name", name);
                startActivity(intent);
                finish();
            }
        });
    }
}