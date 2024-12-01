package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecommendMenu extends AppCompatActivity {
    private TextView menu1, menu2, menu3;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_menu);

        menu1 = findViewById(R.id.menu_1);
        menu2 = findViewById(R.id.menu_2);
        menu3 = findViewById(R.id.menu_3);
        backButton = findViewById(R.id.back_button);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendMenu.this, RecommendMenuNum1.class);
                startActivity(intent);
                finish();
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendMenu.this, RecommendMenuNum2.class);
                startActivity(intent);
                finish();
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendMenu.this, RecommendMenuNum3.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendMenu.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}