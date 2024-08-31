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
import java.util.Objects;

public class ItemData extends AppCompatActivity {

    TextView viewItemName, itemKcalo, itemProtein, itemLipid, itemGlucid, itemUnitType, itemUnitName;

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
        itemUnitType = findViewById(R.id.unit_type);
        itemUnitName = findViewById(R.id.unit_name);
        itemKcalo = findViewById(R.id.item_kcalo);
        itemProtein = findViewById(R.id.item_protein);
        itemLipid = findViewById(R.id.item_lipid);
        itemGlucid = findViewById(R.id.item_glucid);
        itemAmount = findViewById(R.id.item_amount);
        itemImage = findViewById(R.id.item_image);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String itemName = "", unitType = "", unitName = "";
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
            unitType = intent.getStringExtra("UnitType");
        }

        viewItemName.setText(itemName);

        itemImage.setImageResource(imageId);
        
        if(Objects.equals(unitType, "(g)")) {
            unitName = "Khối lượng";
        } else {
            unitName = "Thể tích";
        }

        itemUnitType.setText(unitType);
        itemUnitName.setText(unitName);

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

//        Toast.makeText(ItemData.this, kcal + " " + protein + " " + lipid + " " + glucid, Toast.LENGTH_SHORT).show();

        String finalItemName = itemName;
        String finalUnitType = unitType;
        addToDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemData.this, FragmentDiary.class);
                intent.putExtra("Name", finalItemName);
                intent.putExtra("Kcal", kcal);
                intent.putExtra("Protein", protein);
                intent.putExtra("Lipid", lipid);
                intent.putExtra("Glucid", glucid);
                intent.putExtra("UnitType", finalUnitType);
                startActivity(intent);
            }
        });

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