package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

public class Dietary extends AppCompatActivity {

    Button backButton;
    
    TabLayout tabLayout;

    ViewPager viewPager;
    
    FragmentGroceries fragmentGroceries;
    
    FragmentDiary fragmentDiary;
    
    FragmentDrinks fragmentDrinks;
    
    FragmentFood fragmentFood;
    
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietary);

        backButton = findViewById(R.id.back_button);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.pageViewer);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FragmentFood(), "Món ăn");
        vpAdapter.addFragment(new FragmentGroceries(), "Thực phẩm");
        vpAdapter.addFragment(new FragmentDrinks(), "Sữa / Đồ uống");
        vpAdapter.addFragment(new FragmentDiary(), "Nhật ký");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dietary.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}