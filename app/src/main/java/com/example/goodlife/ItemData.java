package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class ItemData extends AppCompatActivity {

    TextView viewItemName, itemKcalo, itemProtein, itemLipid, itemGlucid;

    TextInputEditText itemAmount;

    Button backButton, calculateButton, addToDiaryButton;

    ImageView itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_data);

        backButton = findViewById(R.id.back_button);
        calculateButton = findViewById(R.id.calc_button);
        addToDiaryButton = findViewById(R.id.add_to_diary_button);
        viewItemName = findViewById(R.id.item_name);
        itemKcalo = findViewById(R.id.item_kcalo);
        itemProtein = findViewById(R.id.item_protein);
        itemLipid = findViewById(R.id.item_lipid);
        itemGlucid = findViewById(R.id.item_glucid);
        itemAmount = findViewById(R.id.item_amount);
        itemImage = findViewById(R.id.item_image);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String itemName = "";
        final int[] kcal = {0};
        int imageId = 0;
        final double[] protein = {0};
        final double[] lipid = { 0 };
        final double[] glucid = { 0 };
        if(bundle != null) {
            itemName = intent.getStringExtra("Name");
            kcal[0] = intent.getIntExtra("Kcal", 0);
            protein[0] = intent.getDoubleExtra("Protein", 0);
            lipid[0] = intent.getDoubleExtra("Lipid", 0);
            glucid[0] = intent.getDoubleExtra("Glucid", 0);
            imageId = intent.getIntExtra("Image", 0);
        }

        viewItemName.setText(itemName);

//        itemKcalo.setText(String.valueOf(kcal));
//        itemProtein.setText(String.valueOf(protein));
//        itemLipid.setText(String.valueOf(lipid));
//        itemGlucid.setText(String.valueOf(glucid));

        itemImage.setImageResource(imageId);

        final Double[] amount = new Double[1];

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(!itemAmount.getText().toString().equals("")) {
//                    amount[0] = Double.parseDouble(itemAmount.getText().toString());
//                } else {
//                    amount[0] = 0.0;
//                }

                amount[0] = Double.parseDouble(itemAmount.getText().toString());

//                kcal[0] = (int) ((kcal[0] * amount[0]) / 100);
//                protein[0] = (protein[0] * amount[0]) / 100;
//                lipid[0] = (lipid[0] * amount[0]) / 100;
//                glucid[0] = (glucid[0] * amount[0]) / 100;

                DecimalFormat decimalFormat = new DecimalFormat("0.0");

                String kcalValue = String.valueOf((int) ((kcal[0] * amount[0]) / 100));
                String proteinValue = decimalFormat.format((protein[0] * amount[0]) / 100);
                String lipidValue = decimalFormat.format((lipid[0] * amount[0]) / 100);
                String glucidValue = decimalFormat.format((glucid[0] * amount[0]) / 100);

                itemKcalo.setText(kcalValue);
                itemProtein.setText(proteinValue);
                itemLipid.setText(lipidValue);
                itemGlucid.setText(glucidValue);
            }
        });

        viewItemName.setText(itemName);

//        itemKcalo.setText(String.valueOf(kcal[0]));
//        itemProtein.setText(String.valueOf(protein[0]));
//        itemLipid.setText(String.valueOf(lipid[0]));
//        itemGlucid.setText(String.valueOf(glucid[0]));

        itemImage.setImageResource(imageId);

//        Toast.makeText(ItemData.this, kcal + " " + protein + " " + lipid + " " + glucid, Toast.LENGTH_SHORT).show();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemData.this, Dietary.class);
                startActivity(intent);
                finish();
            }
        });
    }
}