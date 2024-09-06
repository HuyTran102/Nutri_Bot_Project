package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class HomePage extends AppCompatActivity {

    Button nutritionalStatus, physicalActivity, dietary;

    ProgressBar weightProgressBar, heightProgressBar, kcaloProgressBar;

    TextView weightProgressText, heightProgressText, kcaloProgressText, weightView, heightView;

    double actualWeight = 0, actualHeight = 0, recommendWeight = 0, recommendHeight = 0;

    int weight = 0, height = 0, kcalo = 0;

    String weight_status = "", height_status = "";

    double statusWeight = 0, statusHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        nutritionalStatus = findViewById(R.id.nutritionalStatusButton);
        physicalActivity = findViewById(R.id.physicalActivityButton);
        dietary = findViewById(R.id.dietaryButton);
        weightView = findViewById(R.id.weight_view);
        heightView = findViewById(R.id.height_view);
        weightProgressBar = findViewById(R.id.weight_progres_bar);
        weightProgressText = findViewById(R.id.weight_progres_text);
        heightProgressBar = findViewById(R.id.height_progres_bar);
        heightProgressText = findViewById(R.id.height_progres_text);
        kcaloProgressBar = findViewById(R.id.kcalo_progres_bar);
        kcaloProgressText = findViewById(R.id.kcalo_progres_text);

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        actualHeight = Double.parseDouble(sharedPreferences.getString("Height", "0"));
        actualWeight = Double.parseDouble(sharedPreferences.getString("Weight", "0"));
        recommendHeight = Double.parseDouble(sharedPreferences.getString("RecommendHeight", "0"));
        recommendWeight = Double.parseDouble(sharedPreferences.getString("RecommendWeight", "0"));

        actualHeight *= 100;

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        if(recommendWeight == 0){
            statusWeight = 0;
            weight_status = "Bình thường";
        } else if(actualWeight > recommendWeight) {
            statusWeight = (actualWeight - recommendWeight);
            weight_status = "Thừa " + decimalFormat.format(statusWeight) + " (kg)";

        } else if(actualWeight < recommendWeight){
            statusWeight = (recommendWeight - actualWeight);
            weight_status = "Thiếu " + decimalFormat.format(statusWeight) + " (kg)";
        }

        if (recommendHeight == 0){
            statusHeight = 0;
            height_status = "Bình thường";
        } else if(actualHeight > recommendHeight) {
            statusHeight = (actualHeight - recommendHeight);
            height_status = "Thừa " + decimalFormat.format(statusHeight) + " (cm)";
        } else if(actualHeight < recommendHeight){
            statusHeight = (recommendHeight - actualHeight);
            height_status = "Thừa " + decimalFormat.format(statusHeight) + " (cm)";
        }

        final Handler handler = new Handler();

        weightProgressBar.setMax((int) recommendWeight);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(weight <= actualWeight) {
                    weightProgressText.setText(String.valueOf(weight));
                    weightProgressBar.setProgress(weight);
                    weight++;
                    handler.postDelayed(this, 0);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        }, 0);

        weightView.setText(weight_status);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(height <= actualHeight) {
                    heightProgressText.setText(String.valueOf(height));
                    heightProgressBar.setProgress(height);
                    height++;
                    handler.postDelayed(this, 0);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        }, 0);

        heightView.setText(height_status);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(kcalo <= 50) {
                    kcaloProgressText.setText(String.valueOf(kcalo));
                    kcaloProgressBar.setProgress(kcalo);
                    kcalo++;
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