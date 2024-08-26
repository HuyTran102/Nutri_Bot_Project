package com.example.goodlife;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    ImageView image;

    TextView text;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.imageview);
        text = itemView.findViewById(R.id.name);
    }

}
