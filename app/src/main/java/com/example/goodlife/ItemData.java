package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemData extends AppCompatActivity {

    TextView viewItemName, itemKcalo, itemProtein, itemLipid, itemGlucid;

    Button backButton;

    ImageView itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_data);

        backButton = findViewById(R.id.back_button);
        viewItemName = findViewById(R.id.item_name);
        itemKcalo = findViewById(R.id.item_kcalo);
        itemProtein = findViewById(R.id.item_protein);
        itemLipid = findViewById(R.id.item_lipid);
        itemGlucid = findViewById(R.id.item_glucid);
        itemImage = findViewById(R.id.item_image);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String itemName = "";
        int kcal = 0, imageId = 0;
        double protein = 0, lipid = 0, glucid = 0;
        if(bundle != null) {
            itemName = intent.getStringExtra("Name");
            kcal = intent.getIntExtra("Kcal", 0);
            protein = intent.getDoubleExtra("Protein", 0);
            lipid = intent.getDoubleExtra("Lipid", 0);
            glucid = intent.getDoubleExtra("Glucid", 0);
            imageId = intent.getIntExtra("Image", 0);
        }

        viewItemName.setText(itemName);
        itemKcalo.setText(String.valueOf(kcal));
        itemProtein.setText(String.valueOf(protein));
        itemLipid.setText(String.valueOf(lipid));
        itemGlucid.setText(String.valueOf(glucid));

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