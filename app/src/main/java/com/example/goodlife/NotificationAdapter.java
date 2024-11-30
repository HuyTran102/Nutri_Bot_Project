package com.example.goodlife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {
    Context context;
    List<NotificationData> items;

    public NotificationAdapter(Context context, List<NotificationData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHolder(LayoutInflater.from(context).inflate(R.layout.notifiation_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        NotificationData itemAtPosition = items.get(position);
        holder.name.setText(itemAtPosition.getName());
        holder.information.setText(itemAtPosition.getInformation());
        holder.time.setText(itemAtPosition.getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
