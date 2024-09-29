package com.example.goodlife;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SplitViewHolder extends RecyclerView.ViewHolder{
    TextView date;
    private final int viewType;

    public SplitViewHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;

        date = itemView.findViewById(R.id.date);
    }

    public int getViewType() {
        return viewType;
    }
}
