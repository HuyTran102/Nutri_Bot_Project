package com.example.goodlife;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentDiary extends Fragment {

    TextView viewItemName, itemKcalo, itemProtein, itemLipid, itemGlucid, itemUnitType, itemUnitName, itemAmount;
    List<DiaryItem> items = new ArrayList<>();

    String itemName, unitName, unitType;
    RecyclerView recyclerView;

    int kcal = 0 , calories_val = 0;
    double protein_val = 0, lipid_val = 0, glucid_val = 0, amount_val = 0;

    double amount, protein, lipid, glucid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.fragment_diary, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycleView);
        viewItemName = getView().findViewById(R.id.item_name);
        itemAmount = getView().findViewById(R.id.item_amount);
        itemUnitType = getView().findViewById(R.id.unit_type);
        itemUnitName = getView().findViewById(R.id.unit_name);
        itemKcalo = getView().findViewById(R.id.item_kcalo);
        itemProtein = getView().findViewById(R.id.item_protein);
        itemLipid = getView().findViewById(R.id.item_lipid);
        itemGlucid = getView().findViewById(R.id.item_glucid);

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("DiaryData", Context.MODE_PRIVATE);

        itemName = sharedPreferences.getString("Name", null);
        unitName = sharedPreferences.getString("UnitName", null);
        unitType = sharedPreferences.getString("UnitType", null);

        kcal = sharedPreferences.getInt("Kcal", 0);

        amount = sharedPreferences.getFloat("Amount", 0);
        protein = sharedPreferences.getFloat("Protein", 0);
        lipid = sharedPreferences.getFloat("Lipid", 0);
        glucid = sharedPreferences.getFloat("Glucid", 0);

        // set total value
        calories_val += kcal;
        amount_val += amount;
        protein_val += protein;
        lipid_val += lipid;
        glucid_val += glucid;

        itemKcalo.setText(String.valueOf(calories_val));
        DecimalFormat df = new DecimalFormat("###.#");
        itemAmount.setText(df.format(amount_val));
        itemGlucid.setText(df.format(glucid_val));
        itemLipid.setText(df.format(lipid_val));
        itemProtein.setText(df.format(protein_val));


        DiaryItem diaryItem = new DiaryItem(itemName, amount, kcal, protein, lipid, glucid, unitType, unitName);
        items.add(diaryItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DiaryViewAdapter(getContext(), items));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }
}