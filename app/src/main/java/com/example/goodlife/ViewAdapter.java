package com.example.goodlife;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    Context context;

    List<Item> items;

    public ViewAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageResource(items.get(position).getImage());
        holder.text.setText(items.get(position).getName());
        Item itemAtPosition = items.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemData.class);
                intent.putExtra("Name", itemAtPosition.name);
                intent.putExtra("Kcal", itemAtPosition.kcal);
                intent.putExtra("Protein", itemAtPosition.protein);
                intent.putExtra("Lipid", itemAtPosition.lipid);
                intent.putExtra("Glucid", itemAtPosition.glucid);
                intent.putExtra("Image", itemAtPosition.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
