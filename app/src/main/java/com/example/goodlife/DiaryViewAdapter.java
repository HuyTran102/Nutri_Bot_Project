package com.example.goodlife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class DiaryViewAdapter extends RecyclerView.Adapter<DiaryViewHolder> {

    Context context;

    List<DiaryItem> items;

    public DiaryViewAdapter(Context context, List<DiaryItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiaryViewHolder(LayoutInflater.from(context).inflate(R.layout.diary_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DiaryItem itemAtPosition = items.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        holder.name.setText(itemAtPosition.getName());
        holder.unit_type.setText(itemAtPosition.getUnit_type());
        holder.unit_name.setText(itemAtPosition.getUnit_name());
        holder.amount.setText(decimalFormat.format(itemAtPosition.getAmount()));
        holder.kcal.setText(decimalFormat.format(itemAtPosition.getKcal()));
        holder.protein.setText(decimalFormat.format(itemAtPosition.getProtein()));
        holder.lipid.setText(decimalFormat.format(itemAtPosition.getLipid()));
        holder.glucid.setText(decimalFormat.format(itemAtPosition.getGlucid()));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.remove(position);

                notifyDataSetChanged();
                Intent intent = new Intent(context, Dietary.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
