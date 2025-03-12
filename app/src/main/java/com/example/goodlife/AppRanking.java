package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

        // Make status bar fully transparent
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));

        // Set the layout to extend into the status bar
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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