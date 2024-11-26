package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class AppRanking extends AppCompatActivity {
    private RatingBar ratingBar;
    private TextView message;
    private Button backButton, submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_ranking);

        ratingBar = findViewById(R.id.app_rating);
        message = findViewById(R.id.message);
        submitButton = findViewById(R.id.submit_button);
        backButton = findViewById(R.id.back_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText("Cảm ơn bạn!");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppRanking.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}