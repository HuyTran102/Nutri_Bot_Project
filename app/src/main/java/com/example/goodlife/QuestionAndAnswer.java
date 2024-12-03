package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class QuestionAndAnswer extends AppCompatActivity {
    private LinearLayout basicNutrition, nutritionSchoolHealth, examSeasonNutrition, nutritionPhysicalActivity, nutritionMentalHealth
            , nutritionPubertyPeriod, malnutrition, overWeight;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_and_answer);

        basicNutrition = findViewById(R.id.basic_nutrition);
        nutritionSchoolHealth = findViewById(R.id.nutrition_school_health);
        examSeasonNutrition = findViewById(R.id.exam_season_nutrition);
        nutritionPhysicalActivity = findViewById(R.id.nutrition_physical_activity);
        nutritionPubertyPeriod = findViewById(R.id.nutrition_puberty_period);
        nutritionMentalHealth = findViewById(R.id.nutrition_mental_health);
        malnutrition = findViewById(R.id.malnutrition);
        overWeight = findViewById(R.id.over_weigth);
        backButton = findViewById(R.id.back_button);

        basicNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionAndAnswer.this, BasicNutrition.class);
                startActivity(intent);
                finish();
            }
        });

        nutritionSchoolHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionAndAnswer.this, NutritionSchoolHealth.class);
                startActivity(intent);
                finish();
            }
        });

        examSeasonNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionAndAnswer.this, ExamSeasonNutrition.class);
                startActivity(intent);
                finish();
            }
        });

        nutritionPhysicalActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionAndAnswer.this, NutritionPhysicalActivity.class);
                startActivity(intent);
                finish();
            }
        });

        nutritionMentalHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionAndAnswer.this, NutritionMentalHealth.class);
                startActivity(intent);
                finish();
            }
        });

        nutritionPubertyPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionAndAnswer.this, NutritionPubertyPeriod.class);
                startActivity(intent);
                finish();
            }
        });

        malnutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionAndAnswer.this, Malnutrition.class);
                startActivity(intent);
                finish();
            }
        });

        overWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionAndAnswer.this, OverWeight.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionAndAnswer.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}