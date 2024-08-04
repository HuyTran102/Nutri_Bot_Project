package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    TextView signIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        signUp = findViewById(R.id.sign_up);
        signIn = findViewById(R.id.sign_in);
        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, RegisterPage.class);
            startActivity(intent);
            finish();
        });
    }
}