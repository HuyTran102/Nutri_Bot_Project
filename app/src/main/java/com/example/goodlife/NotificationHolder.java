package com.example.goodlife;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationHolder extends RecyclerView.ViewHolder {
    public TextView name, information, time;

    public NotificationHolder(@NonNull View itemView, int viewType) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        information = itemView.findViewById(R.id.information);
        time = itemView.findViewById(R.id.time);
    }
}
