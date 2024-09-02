package com.example.goodlife;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DiaryViewHolder extends RecyclerView.ViewHolder {

    TextView name, unit_type, unit_name, kcal, amount, protein, lipid, glucid;

    Button delete;

    public DiaryViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.item_view);
        unit_type = itemView.findViewById(R.id.unit_type);
        unit_name = itemView.findViewById(R.id.unit_name);
        kcal = itemView.findViewById(R.id.item_kcalo);
        amount = itemView.findViewById(R.id.item_amount);
        protein = itemView.findViewById(R.id.item_protein);
        lipid = itemView.findViewById(R.id.item_lipid);
        glucid = itemView.findViewById(R.id.item_glucid);

        delete = itemView.findViewById(R.id.delete_item_button);
    }
}
