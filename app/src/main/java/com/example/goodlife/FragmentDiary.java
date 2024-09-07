package com.example.goodlife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentDiary extends Fragment {

    List<DiaryItem> items = new ArrayList<>();

    String itemName, unitName, unitType;

    int kcal;

    double amount, protein, lipid, glucid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.fragment_diary, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycleView);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("DiaryData", Context.MODE_PRIVATE);

        itemName = sharedPreferences.getString("Name", null);
        unitName = sharedPreferences.getString("UnitName", null);
        unitType = sharedPreferences.getString("UnitType", null);

        kcal = sharedPreferences.getInt("Kcal", 0);

        amount = sharedPreferences.getFloat("Amount", 0);
        protein = sharedPreferences.getFloat("Protein", 0);
        lipid = sharedPreferences.getFloat("Lipid", 0);
        glucid = sharedPreferences.getFloat("GLucid", 0);

        DiaryItem diaryItem = new DiaryItem(itemName, amount, kcal, protein, lipid, glucid, unitType, unitName);
        items.add(diaryItem);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DiaryViewAdapter(getContext(), items));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }
}