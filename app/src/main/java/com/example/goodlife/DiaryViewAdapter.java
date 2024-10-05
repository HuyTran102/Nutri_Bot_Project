package com.example.goodlife;

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

public class DiaryViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<DiaryItem> items;
    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;

    public DiaryViewAdapter(Context context, List<DiaryItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return LAYOUT_ONE;
        } else {
            return LAYOUT_TWO;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        if(viewType == LAYOUT_ONE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_split_bar, parent, false);
            viewHolder = new SplitViewHolder(view, viewType);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_recycler_item, parent, false);
            viewHolder = new DiaryViewHolder(view, viewType);
        }

        return viewHolder;
    }

    // Convert and format from date to String
    private String makeDateString(int day, int month, int year) {
        return " " + day + "/" + month + "/" + year + " ";
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DiaryItem itemAtPosition = items.get(position);

        if(holder.getItemViewType() == LAYOUT_ONE) {
            SplitViewHolder splitHolder = (SplitViewHolder) holder;

            splitHolder.date.setText(makeDateString(itemAtPosition.getAdding_day(), itemAtPosition.getAdding_month(), itemAtPosition.getAdding_year()));
        } else {
            DiaryViewHolder itemHolder = (DiaryViewHolder) holder;
            
            SharedPreferences sp = context.getSharedPreferences("Data", Context.MODE_PRIVATE);

            String user_name = sp.getString("Name",null);

            DecimalFormat decimalFormat = new DecimalFormat("0.0");

            itemHolder.name.setText(itemAtPosition.getName());
            itemHolder.unit_type.setText(itemAtPosition.getUnit_type());
            itemHolder.unit_name.setText(itemAtPosition.getUnit_name());
            itemHolder.amount.setText(decimalFormat.format(itemAtPosition.getAmount()));
            itemHolder.kcal.setText(decimalFormat.format(itemAtPosition.getKcal()));
            itemHolder.protein.setText(decimalFormat.format(itemAtPosition.getProtein()));
            itemHolder.lipid.setText(decimalFormat.format(itemAtPosition.getLipid()));
            itemHolder.glucid.setText(decimalFormat.format(itemAtPosition.getGlucid()));

            // set when click on delete button to delete item from database
            itemHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                    // get the document to delete
                    DocumentReference documentReference = firebaseFirestore.collection("GoodLife").document(user_name).collection("Nhật kí").document(itemAtPosition.getName());

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

            itemHolder.infomation.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}