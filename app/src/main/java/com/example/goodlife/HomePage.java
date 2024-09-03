package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    Button nutritionalStatus, physicalActivity, dietary;

    ProgressBar weightProgressBar, heightProgressBar, kcaloProgressBar;

    TextView weightProgressText, heightProgressText, kcaloProgressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        nutritionalStatus = findViewById(R.id.nutritionalStatusButton);
        physicalActivity = findViewById(R.id.physicalActivityButton);
        dietary = findViewById(R.id.dietaryButton);
        weightProgressBar = findViewById(R.id.weight_progres_bar);
        weightProgressText = findViewById(R.id.weight_progres_text);
        heightProgressBar = findViewById(R.id.height_progres_bar);
        heightProgressText = findViewById(R.id.height_progres_text);
        kcaloProgressBar = findViewById(R.id.kcalo_progres_bar);
        kcaloProgressText = findViewById(R.id.kcalo_progres_text);

        final Handler handler = new Handler();

        final int[] weight = {0};
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(weight[0] <= 50) {
                    weightProgressText.setText("" + weight[0]);
                    weightProgressBar.setProgress(weight[0]);
                    weight[0]++;
                    handler.postDelayed(this, 0);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        }, 0);

        final int[] height = {0};

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(height[0] <= 50) {
                    heightProgressText.setText("" + height[0]);
                    heightProgressBar.setProgress(height[0]);
                    height[0]++;
                    handler.postDelayed(this, 0);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        }, 0);

        final int[] kcalo = {0};

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(kcalo[0] <= 50) {
                    kcaloProgressText.setText("" + kcalo[0]);
                    kcaloProgressBar.setProgress(kcalo[0]);
                    kcalo[0]++;
                    handler.postDelayed(this, 0);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        }, 0);

        nutritionalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, CalculateNutritionalStatus.class);
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