package com.example.goodlife;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentDiary extends Fragment {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    TextView viewItemName, itemKcalo, itemProtein, itemLipid, itemGlucid, itemUnitType, itemUnitName, itemAmount;
    List<DiaryItem> items = new ArrayList<>();
    String name;
    RecyclerView recyclerView;
    int calories_val = 0;
    double protein_val = 0, lipid_val = 0, glucid_val = 0, amount_val = 0;

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

        SharedPreferences sp = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name",null);

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        LoadDataFireBase();

        // set total value
        for(DiaryItem diaryItem : items) {
            calories_val += diaryItem.getKcal();
            amount_val += diaryItem.getAmount();
            protein_val += diaryItem.getProtein();
            lipid_val += diaryItem.getLipid();
            glucid_val += diaryItem.getGlucid();
        }

        itemKcalo.setText(String.valueOf(calories_val));
        DecimalFormat df = new DecimalFormat("###.#");
        itemAmount.setText(df.format(amount_val));
        itemGlucid.setText(df.format(glucid_val));
        itemLipid.setText(df.format(lipid_val));
        itemProtein.setText(df.format(protein_val));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DiaryViewAdapter(getContext(), items));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    // Convert and format from date to String
    private String makeDateString(int day, int month, int year) {
        return " " + month + "/" + day + "/" + year + " ";
    }

    // Load Data to Recycle Item
    public  void LoadDataFireBase(){

        firebaseFirestore.collection(name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            // Loop through all documents
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                DiaryItem diaryItem = new DiaryItem(document.getString("name"), Double.parseDouble(document.getString("amount"))
                                        , Integer.parseInt(document.getString("kcal")), Double.parseDouble(document.getString("protein"))
                                        , Double.parseDouble(document.getString("lipid")), Double.parseDouble(document.getString("glucid"))
                                        , document.getString("unit_type"), document.getString("unit_name"), Integer.parseInt(document.getString("image_id")));
                                items.add(diaryItem);
//                                Toast.makeText(getContext(), document.getString("kcal"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w("Firestore", "Error getting documents", task.getException());
                        }
                    }
                });
    }
}