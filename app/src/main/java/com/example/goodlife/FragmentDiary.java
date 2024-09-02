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

public class FragmentDiary extends Fragment {

    List<DiaryItem> items = new ArrayList<>();

    private static final String TAG = "ExcelRead";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        String path = "Groceries.xlsx";

        try {

            AssetManager am = getContext().getAssets();
            InputStream fileInputStream = am.open(path);

            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for(int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows() - 1; rowIndex ++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(1);
                String value = cell.getStringCellValue();
                cell = row.getCell(2);
                int kcal = (int) cell.getNumericCellValue();
                cell = row.getCell(3);
                double protein = (double) cell.getNumericCellValue();
                cell = row.getCell(4);
                double lipid = (double) cell.getNumericCellValue();
                cell = row.getCell(5);
                double glucid = (double) cell.getNumericCellValue();
                cell = row.getCell(6);
                int unit = (int) cell.getNumericCellValue();

                String unit_type;
                if(unit == 0) {
                    unit_type = "(g)";
                } else {
                    unit_type = "(ml)";
                }
                items.add(new DiaryItem(String.valueOf(value), 100, kcal, protein, lipid, glucid, unit_type, "Khối lượng"));
            }
            fileInputStream.close();

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycleView);

//
//        items.add(new DiaryItem("Sữa chưa TH việt quất ha ha", 100, 64, 11, 1.6, 1.6, "(g)", "Khối lượng"));
//        items.add(new DiaryItem("Sữa chưa TH việt quất", 100, 64, 11, 1.6, 1.6, "(g)", "Khối lượng"));
//        items.add(new DiaryItem("Sữa chưa TH việt quất", 100, 64, 11, 1.6, 1.6, "(g)", "Khối lượng"));
//        items.add(new DiaryItem("Sữa chưa TH việt quất", 100, 64, 11, 1.6, 1.6, "(g)", "Khối lượng"));
//        items.add(new DiaryItem("Sữa chưa TH việt quất", 100, 64, 11, 1.6, 1.6, "(g)", "Khối lượng"));
//        items.add(new DiaryItem("Sữa chưa TH việt quất", 100, 64, 11, 1.6, 1.6, "(g)", "Khối lượng"));


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DiaryViewAdapter(getContext(), items));

    }
}