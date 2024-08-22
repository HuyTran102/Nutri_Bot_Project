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
        items.add(new Item("Huy"));
        items.add(new Item("Hiếu"));
        items.add(new Item("Nam"));
        items.add(new Item("Hiếu"));
        items.add(new Item("Hằng"));
        items.add(new Item("Hiền"));
        items.add(new Item("Hằng"));
        items.add(new Item("Thảo"));
        items.add(new Item("Hằng"));
        items.add(new Item("Linh"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ViewAdapter(getContext(), items));

    }
}