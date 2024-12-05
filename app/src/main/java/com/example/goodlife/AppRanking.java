package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AppRanking extends AppCompatActivity {
    private RatingBar ratingBar;
    private TextView message;
    private ImageView ratingIcon;
    private Button backButton, submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_ranking);

        ratingBar = findViewById(R.id.app_rating);
        message = findViewById(R.id.message);
        ratingIcon = findViewById(R.id.rating_icon);
        submitButton = findViewById(R.id.submit_button);
        backButton = findViewById(R.id.back_button);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                if(rating == 1) {
                    ratingIcon.setImageResource(R.drawable.star_1);
                } else if(rating == 2) {
                    ratingIcon.setImageResource(R.drawable.star_2);
                } else if(rating == 3) {
                    ratingIcon.setImageResource(R.drawable.star_3);
                } else if(rating == 4) {
                    ratingIcon.setImageResource(R.drawable.star_4);
                } else if(rating == 5){
                    ratingIcon.setImageResource(R.drawable.star_5);
                } else {
                    ratingIcon.setImageResource(0);
                }
            }
        });

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