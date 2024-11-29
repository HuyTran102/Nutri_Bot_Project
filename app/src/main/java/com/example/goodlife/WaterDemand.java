package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class WaterDemand extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String name;
    private double recommendWeight, recommendWaterAmount;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_demand);
        
        backButton = findViewById(R.id.back_button);

        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name", null);

        Task<QuerySnapshot> nutritionTask = firebaseFirestore.collection("GoodLife")
                .document(name).collection("Dinh dưỡng")
                .get();

        Tasks.whenAll(nutritionTask).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot nutritionResult = nutritionTask.getResult();
                if (nutritionResult != null) {
                    for (QueryDocumentSnapshot document : nutritionResult) {
                        if (document.getString("userRecommendWeight") != null) {
                            try {
                                recommendWeight = Double.parseDouble(document.getString("userRecommendWeight"));

                                recommendWaterAmount = 40 * recommendWeight;

                                Toast.makeText(this, " " + recommendWaterAmount + " " + recommendWeight + " ", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                recommendWeight = 0.0;
                            }
                        }
                    }
                }
            }
        });

//        Toast.makeText(this, " " + recommendWaterAmount + " " + recommendWeight + " ", Toast.LENGTH_SHORT).show();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WaterDemand.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}