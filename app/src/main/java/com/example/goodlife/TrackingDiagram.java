package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class TrackingDiagram extends AppCompatActivity {
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_diagram);

        backButton = findViewById(R.id.back_button);

        LineChart lineChart = findViewById(R.id.lineChart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 2));
        entries.add(new Entry(1, 4));
        entries.add(new Entry(2, 6));
        entries.add(new Entry(3, 3));
        entries.add(new Entry(4, 10));
        entries.add(new Entry(5, 2));
        entries.add(new Entry(6, 1));
        entries.add(new Entry(7, 6));
        entries.add(new Entry(8, 5));
        entries.add(new Entry(9, 13));

        LineDataSet dataSet = new LineDataSet(entries, "Thống kê năng lượng");
        dataSet.setColor(getResources().getColor(R.color.red_pink));
        dataSet.setLineWidth(4f);
        dataSet.setValueTextColor(getResources().getColor(R.color.dark_green));
        dataSet.setValueTextSize(15f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.invalidate();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrackingDiagram.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}