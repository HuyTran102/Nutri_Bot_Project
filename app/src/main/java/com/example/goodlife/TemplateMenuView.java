package com.example.goodlife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class TemplateMenuView extends AppCompatActivity {
    private Button backButton;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private TempMenuViewAdapter viewAdapter;
    private List<TempMenuItem> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_menu_view);

        // Make status bar fully transparent
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));

        // Set the layout to extend into the status bar
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        searchView = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recycleView);
        backButton = findViewById(R.id.back_button);

        items.add(new TempMenuItem("Thực đơn 1 - 1200 Kcal", R.drawable.menu1_1200));
        items.add(new TempMenuItem("Thực đơn 2 - 1200 Kcal", R.drawable.menu2_1200));
        items.add(new TempMenuItem("Thực đơn 3 - 1200 Kcal", R.drawable.menu3_1200));
        items.add(new TempMenuItem("Thực đơn 4 - 1200 Kcal", R.drawable.menu4_1200));
        items.add(new TempMenuItem("Thực đơn 5 - 1200 Kcal", R.drawable.menu5_1200));
        items.add(new TempMenuItem("Thực đơn 1 - 1400 Kcal", R.drawable.menu1_1400));
        items.add(new TempMenuItem("Thực đơn 2 - 1400 Kcal", R.drawable.menu2_1400));
        items.add(new TempMenuItem("Thực đơn 3 - 1400 Kcal", R.drawable.menu3_1400));
        items.add(new TempMenuItem("Thực đơn 4 - 1400 Kcal", R.drawable.menu4_1400));
        items.add(new TempMenuItem("Thực đơn 5 - 1400 Kcal", R.drawable.menu5_1400));
        items.add(new TempMenuItem("Thực đơn 1 - 1600 Kcal", R.drawable.menu1_1600));
        items.add(new TempMenuItem("Thực đơn 2 - 1600 Kcal", R.drawable.menu2_1600));
        items.add(new TempMenuItem("Thực đơn 3 - 1600 Kcal", R.drawable.menu3_1600));
        items.add(new TempMenuItem("Thực đơn 4 - 1600 Kcal", R.drawable.menu4_1600));
        items.add(new TempMenuItem("Thực đơn 5 - 1600 Kcal", R.drawable.menu5_1600));

        viewAdapter = new TempMenuViewAdapter(this, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(viewAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                findData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                findData(newText);
                return true;
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TemplateMenuView.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });
    }

    void findData(String name) {
        List<TempMenuItem> list = new ArrayList<>();
        for(TempMenuItem data : items) {
            if(data.getName().toLowerCase().contains(name.toLowerCase())) {
                list.add(data);
            }
        }
        viewAdapter.updateList(list);
    }

}