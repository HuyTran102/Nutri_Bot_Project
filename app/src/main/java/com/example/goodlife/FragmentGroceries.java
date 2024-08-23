package com.example.goodlife;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentGroceries extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_groceries, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycleView);

        List<Item> items = new ArrayList<>();
        items.add(new Item("Huy", R.drawable.images));
        items.add(new Item("Hiếu", R.drawable.images));
        items.add(new Item("Nam", R.drawable.images));
        items.add(new Item("Hiếu", R.drawable.images));
        items.add(new Item("Hằng", R.drawable.images));
        items.add(new Item("Hiền", R.drawable.images));
        items.add(new Item("Hằng", R.drawable.images));
        items.add(new Item("Thảo", R.drawable.images));
        items.add(new Item("Hằng", R.drawable.images));
        items.add(new Item("Linh", R.drawable.images));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ViewAdapter(getContext(), items));

    }
}