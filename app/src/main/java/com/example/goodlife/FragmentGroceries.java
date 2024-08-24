package com.example.goodlife;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentGroceries extends Fragment {

    private static final String TAG = "ExcelRead";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_groceries, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycleView);

        List<Item> items = new ArrayList<>();
//        items.add(new Item("Huy", R.drawable.images));
//        items.add(new Item("Hiếu", R.drawable.images));
//        items.add(new Item("Nam", R.drawable.images));
//        items.add(new Item("Hiếu", R.drawable.images));
//        items.add(new Item("Hằng", R.drawable.images));
//        items.add(new Item("Hiền", R.drawable.images));
//        items.add(new Item("Hằng", R.drawable.images));
//        items.add(new Item("Thảo", R.drawable.images));
//        items.add(new Item("Hằng", R.drawable.images));
//        items.add(new Item("Linh", R.drawable.images));

        String path = "Groceries.xlsx";

        try {
            AssetManager am = requireContext().getAssets();
            InputStream is = am.open(path);

            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            for(int rowIndex = 3; rowIndex <= sheet.getLastRowNum(); rowIndex ++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(1);
                String value = cell.getStringCellValue();
                items.add(new Item(value, R.drawable.images));
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ViewAdapter(getContext(), items));

    }

}