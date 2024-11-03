package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DiaryItemData extends AppCompatActivity {
    private TextView viewItemName, itemKcalo, itemProtein, itemLipid, itemGlucid, itemUnitType, itemUnitName;
    private TextInputEditText itemAmount;
    private Button backButton, saveButton;
    private ImageView itemImage;
    private String glucidValue , lipidValue, proteinValue, kcalValue, name;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_item_data);

        backButton = findViewById(R.id.back_button);
        itemAmount = findViewById(R.id.item_amount);
        saveButton = findViewById(R.id.save_button);
        viewItemName = findViewById(R.id.item_name);
        itemUnitType = findViewById(R.id.unit_type);
        itemUnitName = findViewById(R.id.unit_name);
        itemKcalo = findViewById(R.id.item_kcalo);
        itemProtein = findViewById(R.id.item_protein);
        itemLipid = findViewById(R.id.item_lipid);
        itemGlucid = findViewById(R.id.item_glucid);
        itemAmount = findViewById(R.id.item_amount);
        itemImage = findViewById(R.id.item_image);

        firebaseFirestore = FirebaseFirestore.getInstance();

        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name",null);

        // get item value fromm intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String itemName = "", unitType = "", unitName;
        final int[] kcal = {0}, view_kcal = {0};
        int imageId = 0;
        final double[] protein = {0}, view_protein = {0}
                , lipid = {0}, view_lipid = {0}
                , glucid = {0}, view_glucid = {0}
                , view_amount = new double[1];

        if(bundle != null) {
            itemName = intent.getStringExtra("Name");
            view_amount[0] = intent.getDoubleExtra("Amount", 0);
            view_kcal[0] = intent.getIntExtra("Kcal", 0);
            view_protein[0] = intent.getDoubleExtra("Protein", 0);
            view_lipid[0] = intent.getDoubleExtra("Lipid", 0);
            view_glucid[0] = intent.getDoubleExtra("Glucid", 0);
            imageId = intent.getIntExtra("Image", 0);
            unitType = intent.getStringExtra("UnitType");
        }

        // Transfer the unit type to the uint name
        if(Objects.equals(unitType, "(g)")) {
            unitName = "Khối lượng";
        } else {
            unitName = "Thể tích";
        }

        // set item name, image, unit name and unit type
        viewItemName.setText(itemName);

        itemImage.setImageResource(imageId);

        itemAmount.setText(String.valueOf(view_amount[0]));

        itemUnitType.setText(unitType);
        itemUnitName.setText(unitName);

        // set item kcalo, protein, lipid, glucid value
        itemKcalo.setText(String.valueOf(view_kcal[0]));
        itemProtein.setText(String.valueOf(view_protein[0]));
        itemLipid.setText(String.valueOf(view_lipid[0]));
        itemGlucid.setText(String.valueOf(view_glucid[0]));

        // change the item value to the default value
        kcal[0] = (int) ((view_kcal[0] * 100) / view_amount[0]);
        protein[0] = (view_protein[0] * 100) / view_amount[0];
        lipid[0] = (view_lipid[0] * 100) / view_amount[0];
        glucid[0] = (view_glucid[0] * 100) / view_amount[0];

        // Make other value change along with the item amount
        final double[] amount = new double[1];
        itemAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(itemAmount.getText().toString().equals("") || Double.parseDouble(itemAmount.getText().toString()) < 0.0){
                    itemKcalo.setText("0");
                    itemGlucid.setText("0");
                    itemProtein.setText("0");
                    itemLipid.setText("0");
                    return;
                }
                amount[0] = Double.parseDouble(itemAmount.getText().toString());

                // Calculate all value with the user item amount
                DecimalFormat decimalFormat = new DecimalFormat("0.0");

                kcalValue = String.valueOf((int) ((kcal[0] * amount[0]) / 100));
                proteinValue = decimalFormat.format((protein[0] * amount[0]) / 100);
                lipidValue = decimalFormat.format((lipid[0] * amount[0]) / 100);
                glucidValue = decimalFormat.format((glucid[0] * amount[0]) / 100);

                // set item new kcalo, protein, lipid, glucid value
                itemKcalo.setText(kcalValue);
                itemProtein.setText(proteinValue);
                itemLipid.setText(lipidValue);
                itemGlucid.setText(glucidValue);
            }
        });

        int finalImageId = imageId;
        String finalItemName = itemName;
        String finalUnitType = unitType;
        String finalUnitName = unitName;

        // save item to database when click on the saveButton
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change the "," in number to "."
                proteinValue = proteinValue.replace(",", ".");
                lipidValue = lipidValue.replace(",", ".");
                glucidValue = glucidValue.replace(",", ".");

                // Get the current date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                // write the data to the database
                WriteDataFireBase(finalItemName, amount[0], kcalValue, proteinValue, lipidValue, glucidValue
                        , finalUnitType, finalUnitName, String.valueOf(finalImageId)
                        , year, month, day);

                Intent intent = new Intent(DiaryItemData.this, Dietary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        // set when click on the back button to went back to Dietary.Java
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryItemData.this, Dietary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    // Write Data to Cloud Firestone
    public void WriteDataFireBase(String itemName, double itemAmount, String itemKcalValue
            , String itemProteinValue,String itemLipidValue, String itemGlucidValue
            , String itemUnitType, String itemUnitName, String itemImageId
            , int itemAddingYear, int itemAddingMonth, int itemAddingDay){
        // Create a new item with all of the data like name, amount, ...
        Map<String, Object> item = new HashMap<>();
        item.put("name", itemName);
        item.put("amount", String.valueOf(itemAmount));
        item.put("kcal", itemKcalValue);
        item.put("protein", itemProteinValue);
        item.put("lipid", itemLipidValue);
        item.put("glucid", itemGlucidValue);
        item.put("unit_name", itemUnitName);
        item.put("unit_type", itemUnitType);
        item.put("image_id", itemImageId);
        item.put("year", String.valueOf(itemAddingYear));
        item.put("month", String.valueOf(itemAddingMonth));
        item.put("day", String.valueOf(itemAddingDay));

        firebaseFirestore.collection("GoodLife").document(name).collection("Nhật kí").document(itemName)
                .set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Adding item to database successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error adding item to database: ", e);
                    }
                });
    }
}