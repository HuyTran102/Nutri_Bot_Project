package com.example.goodlife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        SharedPreferences sp = context.getSharedPreferences("Data", Context.MODE_PRIVATE);

        String user_name = sp.getString("Name",null);

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

        // set when click on delete button to delete item from database
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                // get the document to delete
                DocumentReference documentReference = firebaseFirestore.collection(user_name).document(itemAtPosition.getName());

                // delete document from database
                documentReference.delete()
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firestore", "Deleting item successfully");
                        })
                        .addOnFailureListener(e -> {
                            Log.w("Firestore", "Error deleting document", e);
                        });
                Intent intent = new Intent(context, Dietary.class);
                context.startActivity(intent);
            }
        });

        holder.infomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DiaryItemData.class);
                intent.putExtra("Name", itemAtPosition.name);
                intent.putExtra("Amount", itemAtPosition.amount);
                intent.putExtra("Kcal", itemAtPosition.kcal);
                intent.putExtra("Protein", itemAtPosition.protein);
                intent.putExtra("Lipid", itemAtPosition.lipid);
                intent.putExtra("Glucid", itemAtPosition.glucid);
                intent.putExtra("Image", itemAtPosition.getImage());
                intent.putExtra("UnitType", itemAtPosition.unit_type);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
