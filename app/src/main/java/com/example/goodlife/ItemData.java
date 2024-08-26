package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ItemData extends AppCompatActivity {

    TextView viewItemName;

    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_data);

        backButton = findViewById(R.id.back_button);
        viewItemName = findViewById(R.id.item_name);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String itemName = "";
        if(bundle != null) {
            itemName = intent.getStringExtra("Name");
        }

        viewItemName.setText(itemName);

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