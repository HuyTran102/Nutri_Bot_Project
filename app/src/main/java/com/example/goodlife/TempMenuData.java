package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TempMenuData extends AppCompatActivity {
    private Button backButton;
    private ImageView menuImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_menu_data);

        backButton = findViewById(R.id.back_button);
        menuImage = findViewById(R.id.item_image);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int imageId = 0;

        if(bundle != null) {
            imageId = intent.getIntExtra("Image", 0);
        }

        menuImage.setImageResource(imageId);

        // set when click on the back button to went back to TemplateMenuView.Java
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TempMenuData.this, TemplateMenuView.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                finish();
            }
        });
    }
}