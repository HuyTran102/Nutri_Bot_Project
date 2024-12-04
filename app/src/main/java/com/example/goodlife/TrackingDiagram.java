package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class TrackingDiagram extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ArrayList<Entry> entries = new ArrayList<>();
    private LocalDate currentDate;
    private LineChart lineChart;
    private WeekFields weekFields;
    private LocalDate startOfWeek;
    private LocalDate endOfWeek;
    private String name;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_diagram);

        lineChart = findViewById(R.id.lineChart);
        backButton = findViewById(R.id.back_button);

        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name",null);

        currentDate = LocalDate.now();

        weekFields = WeekFields.of(Locale.getDefault());

        startOfWeek = currentDate.with(weekFields.dayOfWeek(), 1);

        endOfWeek = currentDate.with(weekFields.dayOfWeek(), 7);

        LoadData();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrackingDiagram.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void LoadData() {
        firebaseFirestore.collection("GoodLife")
                .document(name)
                .collection("Nhật kí")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Loop through all documents
                            for (int i = startOfWeek.getDayOfMonth(); i <= endOfWeek.getDayOfMonth(); i++) {
                                int sumKcal = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    int day = Integer.parseInt(document.getString("day"))
                                            , month = Integer.parseInt(document.getString("month"))
                                            , year = Integer.parseInt(document.getString("year"));
                                    if ((i == day && day == i)
                                            && (startOfWeek.getMonthValue() == month && endOfWeek.getMonthValue() == month)
                                            && (startOfWeek.getYear() == year && endOfWeek.getYear() == year)) {
                                        sumKcal += Integer.parseInt(document.getString("kcal"));
                                    }
                                }
                                entries.add(new Entry(i, sumKcal));

                                LineDataSet dataSet = new LineDataSet(entries, "Năng lượng");
                                dataSet.setColor(getResources().getColor(R.color.red_pink));
                                dataSet.setLineWidth(3f);
                                dataSet.setValueTextColor(getResources().getColor(R.color.dark_green));
                                dataSet.setValueTextSize(15f);

                                LineData lineData = new LineData(dataSet);
                                lineChart.setData(lineData);
                                lineChart.getDescription().setEnabled(false);
                                lineChart.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
                                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

//                                if(sumKcal != 0) Toast.makeText(TrackingDiagram.this, "" + sumKcal, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w("Firestore", "Error getting documents", task.getException());
                        }
                    }
                });
    }
}