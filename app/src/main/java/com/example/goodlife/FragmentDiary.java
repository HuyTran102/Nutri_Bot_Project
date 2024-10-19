package com.example.goodlife;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentDiary extends Fragment {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private TextView viewItemName, itemKcalo, itemProtein, itemLipid, itemGlucid, itemUnitType, itemUnitName, itemAmount;
    private String name;
    private RecyclerView recyclerView;
    private int calories_val = 0;
    private int year, month, day;
    private double protein_val = 0, lipid_val = 0, glucid_val = 0, amount_val = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycleView);
        dateButton = view.findViewById(R.id.datePickerButton);
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

        // Get the current date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        month = month + 1;
        // Handle the selected date (e.g., update the TextView)
        String selectedDate = makeDateString(day, month, year);
        showListData(selectedDate);

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        // Get the current date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        month = month + 1;
        // Handle the selected date (e.g., update the TextView)
        String selectedDate = makeDateString(day, month, year);
        showListData(selectedDate);

        LoadDataFireBase();

        calories_val = 0;
        amount_val = 0;
        protein_val = 0;
        lipid_val = 0;
        glucid_val = 0;
    }

    // Convert and format from date to String
    private String makeDateString(int day, int month, int year) {
        return month + "/" + day + "/" + year;
    }

    public void setDataUI(List<DiaryItem> items){

        calories_val = 0;
        amount_val = 0;
        protein_val = 0;
        lipid_val = 0;
        glucid_val = 0;

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

    // Date Picker for user to select their date of birth
    public void LoadDataFireBase () {
        // Create a DatePickerDialog with Holo theme
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                // Handle the selected date (e.g., update the TextView)
                String selectedDate = makeDateString(day, month, year);
                //selectedDateTextView.setText(selectedDate);
                Log.d("DATE",selectedDate);
                showListData(selectedDate);

            }
        };

        // Get the current date
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        // Use Holo theme here
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    // Load Data to Recycle Item
    public void showListData(String selectedDate){
        List<DiaryItem> list_items = new ArrayList<>();
        firebaseFirestore.collection("GoodLife")
                .document(name)
                .collection("Nhật kí")
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
                                        , document.getString("unit_type"), document.getString("unit_name"), Integer.parseInt(document.getString("image_id"))
                                        , Integer.parseInt(document.getString("year")), Integer.parseInt(document.getString("month")), Integer.parseInt(document.getString("day"))
                                        , Integer.parseInt(document.getString("hour")), Integer.parseInt(document.getString("minute")), Integer.parseInt(document.getString("second")));
                                String date = makeDateString(Integer.parseInt(document.getString("day")), Integer.parseInt(document.getString("month")), Integer.parseInt(document.getString("year")));
                                if (date.equals(selectedDate)) {
                                    list_items.add(diaryItem);
                                }
//                                Toast.makeText(getContext(), document.getString("kcal"), Toast.LENGTH_SHORT).show();
                            }

                            String[] date = selectedDate.split("/");

                            DiaryItem startItem = new DiaryItem("", 0, 0, 0, 0, 0
                                    , "", "", 0, Integer.parseInt(date[2]), Integer.parseInt(date[0]), Integer.parseInt(date[1]), 0, 0, 0);

                            list_items.add(0, startItem);
                            
                            Collections.sort(list_items, new Comparator<DiaryItem>() {
                                @Override
                                public int compare(DiaryItem item1, DiaryItem item2) {
                                    LocalTime item1_time = LocalTime.of(item1.getAdding_hour(), item1.getAdding_minute(), item1.getAdding_second());
                                    LocalTime item2_time = LocalTime.of(item2.getAdding_hour(), item2.getAdding_minute(), item2.getAdding_second());
                                    return item1_time.compareTo(item2_time);
                                }
                            });

                            setDataUI(list_items);
                        } else {
                            Log.w("Firestore", "Error getting documents", task.getException());
                        }
                    }
                });
    }
}