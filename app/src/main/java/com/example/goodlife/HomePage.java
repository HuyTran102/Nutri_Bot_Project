package com.example.goodlife;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomePage extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Button nutritionalStatusButton, physicalButton, dietaryButton;
    private ProgressBar weightProgressBar, heightProgressBar, kcaloProgressBar;
    private TextView weightProgressText, heightProgressText, kcaloProgressText, weightView, heightView;
    private double actualWeight, actualHeight, recommendWeight, recommendHeight, statusWeight, statusHeight;
    private int weight, height, kcalo;
    private String weight_status, height_status;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        nutritionalStatusButton = findViewById(R.id.nutritional_status_button);
        physicalButton = findViewById(R.id.physical_button);
        dietaryButton = findViewById(R.id.dietary_button);
        weightView = findViewById(R.id.weight_view);
        heightView = findViewById(R.id.height_view);
        weightProgressBar = findViewById(R.id.weight_progres_bar);
        weightProgressText = findViewById(R.id.weight_progres_text);
        heightProgressBar = findViewById(R.id.height_progres_bar);
        heightProgressText = findViewById(R.id.height_progres_text);
        kcaloProgressBar = findViewById(R.id.kcalo_progres_bar);
        kcaloProgressText = findViewById(R.id.kcalo_progres_text);

//        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
//        actualHeight = Double.parseDouble(sharedPreferences.getString("Height", "0"));
//        actualWeight = Double.parseDouble(sharedPreferences.getString("Weight", "0"));
//        recommendHeight = Double.parseDouble(sharedPreferences.getString("RecommendHeight", "0"));
//        recommendWeight = Double.parseDouble(sharedPreferences.getString("RecommendWeight", "0"));

        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name",null);

        final Handler weight_handler = new Handler();

        weight_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(weight <= actualWeight) {
                    if(recommendWeight == 0) {
                        weightProgressText.setText(String.valueOf(weight + "\n" + 0));
                    } else {
                        weightProgressText.setText(String.valueOf(weight + "\n" + 0));
                    }
                    weightProgressBar.setProgress(weight);
                    weight++;
                    weight_handler.postDelayed(this, 35);
                } else {
                    weight_handler.removeCallbacks(this);
                }
            }
        }, 35);

        weightView.setText(weight_status);

        final Handler height_handler = new Handler();
        height_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(height <= 0) {
                    if(recommendHeight == 0) {
                        heightProgressText.setText(String.valueOf(height + "\n" + 0));
                    } else {
                        heightProgressText.setText(String.valueOf(height + "\n" + 0));
                    }
                    heightProgressBar.setProgress(height);
                    height++;
                    height_handler.postDelayed(this, 35);
                } else {
                    height_handler.removeCallbacks(this);
                }
            }
        }, 35);

        LoadDataFireBase();

        final Handler kcalo_handler = new Handler();

        kcaloProgressBar.setMax(1);

        kcalo_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(kcalo <= 1) {
                    kcaloProgressText.setText(String.valueOf(kcalo));
                    kcaloProgressBar.setProgress(kcalo);
                    kcalo++;
                    kcalo_handler.postDelayed(this, 35);
                } else {
                    kcalo_handler.removeCallbacks(this);
                }
            }
        }, 35);

        nutritionalStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, CalculateNutritionalStatus.class);
                startActivity(intent);
                finish();
            }
        });

        physicalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, Physical.class);
                startActivity(intent);
                finish();
            }
        });

        dietaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, Dietary.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Load Data to Recycle Item
    public void LoadDataFireBase(){
        firebaseFirestore.collection("GoodLife")
                .document(name).collection("Dinh dưỡng")
                .get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if(task.isSuccessful()) {
                        // Loop through all documents
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            if(document.getString("useHeight") == "" && document.getString("userWeight") == ""
                                    && document.getString("userRecommendHeight") == ""
                                    && document.getString("userRecommendWeight") == "") {
                                return;
                            } else {
                                try{
                                    actualHeight = Double.parseDouble(document.getString("userHeight"));
                                }catch (Exception e){
                                    actualHeight = 0.0;
                                }

                                try {
                                    actualWeight = Double.parseDouble(document.getString("userWeight"));
                                }catch (Exception e){
                                    actualWeight = 0.0;
                                }

                                try {
                                    recommendHeight = Double.parseDouble(document.getString("userRecommendHeight"));
                                }catch (Exception e){
                                    recommendHeight = 0.0;
                                }

                                try {
                                    recommendWeight = Double.parseDouble(document.getString("userRecommendWeight"));
                                }catch (Exception e){
                                    recommendWeight = 0.0;
                                }

                                setWeightAndHeight();
                            }

                        }
                    } else {
                        Log.w("Firestore", "Error getting documents", task.getException());
                    }
                });
    }

    public void setWeightAndHeight() {
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
            height_status = "Thiếu " + decimalFormat.format(statusHeight) + " (cm)";
        }

        decimalFormat = new DecimalFormat("0");

        final Handler weight_handler = new Handler();

        weightProgressBar.setMax((int) recommendWeight + 1);

        DecimalFormat finalDecimalFormat = decimalFormat;
        weight_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(weight <= actualWeight) {
                    if(recommendWeight == 0) {
                        weightProgressText.setText(String.valueOf(weight + "\n" + finalDecimalFormat.format(actualWeight)));
                    } else {
                        weightProgressText.setText(String.valueOf(weight + "\n" + finalDecimalFormat.format(recommendWeight)));
                    }
                    weightProgressBar.setProgress(weight);
                    weight++;
                    weight_handler.postDelayed(this, 35);
                } else {
                    weight_handler.removeCallbacks(this);
                }
            }
        }, 35);

        weightView.setText(weight_status);

        final Handler height_handler = new Handler();

        heightProgressBar.setMax((int) recommendHeight + 1);

        DecimalFormat finalDecimalFormat1 = decimalFormat;
        height_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(height <= actualHeight) {
                    if(recommendHeight == 0) {
                        heightProgressText.setText(String.valueOf(height + "\n" + finalDecimalFormat.format(actualHeight)));
                    } else {
                        heightProgressText.setText(String.valueOf(height + "\n" + finalDecimalFormat.format(recommendHeight)));
                    }
                    heightProgressBar.setProgress(height);
                    height++;
                    height_handler.postDelayed(this, 35);
                } else {
                    height_handler.removeCallbacks(this);
                }
            }
        }, 35);

        heightView.setText(height_status);

    }
}