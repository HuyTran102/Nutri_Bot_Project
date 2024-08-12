package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class CalculateNutritionalStatus extends AppCompatActivity {

    Button backButton, resultButton;

    TextInputEditText userHeight, userWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_nutritional_status);

        backButton = findViewById(R.id.back_button);
        resultButton = findViewById(R.id.result_button);
        userHeight = findViewById(R.id.user_height);
        userWeight = findViewById(R.id.user_weight);

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);

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
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Height", String.valueOf(userHeight.getText()));
                editor.putString("Weight", String.valueOf(userWeight.getText()));
                editor.apply();
                Intent intent = new Intent(CalculateNutritionalStatus.this, NutritionalStatusResult.class);
                startActivity(intent);
                finish();
            }
        });
    }
}