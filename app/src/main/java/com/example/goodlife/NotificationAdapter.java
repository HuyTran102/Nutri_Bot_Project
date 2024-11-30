package com.example.goodlife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<NotificationData> items;

    public NotificationAdapter(Context context, List<NotificationData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.notifiation_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotificationData itemAtPosition = items.get(position);
        NotificationHolder itemHolder = (NotificationHolder) holder;
        itemHolder.name.setText(itemAtPosition.getName());
        itemHolder.information.setText(itemAtPosition.getInformation());
        itemHolder.time.setText(itemAtPosition.getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
