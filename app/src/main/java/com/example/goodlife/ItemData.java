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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemData extends AppCompatActivity {
    private TextView viewItemName, itemKcalo, itemProtein, itemLipid, itemGlucid, itemUnitType, itemUnitName;
    private TextInputEditText itemAmount;
    private Button backButton, addToDiaryButton;
    private ImageView itemImage;
    private String glucidValue , lipidValue, proteinValue, kcalValue, name;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_data);

        backButton = findViewById(R.id.back_button);
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
        
        firebaseFirestore = FirebaseFirestore.getInstance();

        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name",null);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String itemName = "", unitType = "", unitName;
        final int[] kcal = {0};
        int imageId = 0;
        final double[] protein = {0};
        final double[] lipid = {0};
        final double[] glucid = {0};

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

        // Transfer the unit type to the uint name
        if(Objects.equals(unitType, "(g)")) {
            unitName = "Khối lượng";
        } else {
            unitName = "Thể tích";
        }

        itemUnitType.setText(unitType);
        itemUnitName.setText(unitName);

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
                if(itemAmount.getText().toString().equals("") || Double.parseDouble(itemAmount.getText().toString()) <= 0.0){
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

                itemKcalo.setText(kcalValue);
                itemProtein.setText(proteinValue);
                itemLipid.setText(lipidValue);
                itemGlucid.setText(glucidValue);

            }
        });

        String finalItemName = itemName;
        String finalUnitType = unitType;
        String finalUnitName = unitName;

        SharedPreferences sharedPreferences = getSharedPreferences("DiaryData", Context.MODE_PRIVATE);
        addToDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Name", finalItemName);
                editor.putString("UnitType", finalUnitType);
                editor.putString("UnitName", finalUnitName);

                editor.putInt("Kcal", Integer.parseInt(kcalValue));

                DecimalFormat decimalFormat = new DecimalFormat("0.0");

                proteinValue = proteinValue.replace(",", ".");
                lipidValue = lipidValue.replace(",", ".");
                glucidValue = glucidValue.replace(",", ".");

                editor.putFloat("Amount", Float.parseFloat(String.valueOf(amount[0])));
                editor.putFloat("Protein", Float.parseFloat(proteinValue));
                editor.putFloat("Lipid", Float.parseFloat(lipidValue));
                editor.putFloat("Glucid", Float.parseFloat(glucidValue));

                WriteDataFireBase(finalItemName, amount[0], kcalValue, proteinValue, lipidValue, glucidValue, finalUnitType, finalUnitName);

                editor.apply();
                Intent intent = new Intent(ItemData.this, Dietary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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

    // Write Data to Cloud Firestone
    public void WriteDataFireBase(String itemName, double itemAmount, String itemKcalValue, String itemProteinValue,String itemLipidValue, String itemGlucidValue, String itemUnitType, String itemUnitName){
        firebaseFirestore.collection(name)
                .add(new DiaryItem(itemName, Float.parseFloat(String.valueOf(itemAmount))
                        , Integer.parseInt(itemKcalValue), Float.parseFloat(itemProteinValue)
                        , Float.parseFloat(itemLipidValue), Float.parseFloat(itemGlucidValue)
                        , itemUnitType, itemUnitName))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
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