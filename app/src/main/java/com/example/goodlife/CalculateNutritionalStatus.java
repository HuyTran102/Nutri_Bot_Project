package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Objects;

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
        backButton = findViewById(R.id.back_button);

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);

        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validUserHeight() && validUserWeight()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Height", String.valueOf(userHeight.getText()));
                    editor.putString("Weight", String.valueOf(userWeight.getText()));
                    editor.apply();
                    Intent intent = new Intent(CalculateNutritionalStatus.this, NutritionalStatusResult.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalculateNutritionalStatus.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public Boolean validUserHeight() {
        String height;
        height = String.valueOf(userHeight.getText());
        if(height.isEmpty()) {
            userHeight.setError("Vui lòng nhập vào chiều cao người dùng!");
            return false;
        } else {
            userHeight.setError(null);
            return true;
        }
    }

    public Boolean validUserWeight() {
        String weight;
        weight = String.valueOf(userWeight.getText());
        if(weight.isEmpty()) {
            userWeight.setError("Vui lòng nhập vào cân nặng người dùng!");
            return false;
        } else {
            userWeight.setError(null);
            return true;
        }
    }
}