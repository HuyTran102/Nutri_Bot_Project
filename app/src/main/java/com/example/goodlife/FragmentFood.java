package com.example.goodlife;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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

public class FragmentFood extends Fragment {
    private RecyclerView recyclerView;
    private List<Item> items = new ArrayList<>();
    private SearchView searchView;
    private ViewAdapter viewAdapter;
    private static final String TAG = "ExcelRead";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_food, container, false);

    }

    public void loadData() {
        if (items.isEmpty()) { // Chỉ tải dữ liệu nếu danh sách rỗng
            new Thread(() -> {
                // Thực hiện lấy dữ liệu (ví dụ: đọc file Excel)
                readExcelFile();
    
                // Cập nhật giao diện trên luồng chính
                requireActivity().runOnUiThread(() -> {
                    viewAdapter.notifyDataSetChanged();
                });
            }).start();
        }
    }

    public void readExcelFile() {
        String path = "Diary.xlsx";

        try {

            AssetManager am = getContext().getAssets();
            InputStream fileInputStream = am.open(path);

            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(1);

            int imageNameIndex1 = 3000, imageNameIndex2 = 1000, imageNameIndex3 = 2000, imageNameIndex4 = 5000, imageNameIndex5 = 15000;

            for(int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows() - 1; rowIndex ++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(1);
                String name = cell.getStringCellValue();
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

                String i_name;

                if(rowIndex >= 0 && rowIndex <= 205) {

                    imageNameIndex4++;

                    i_name = "n" + imageNameIndex4;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 206 && rowIndex <= 269) {

                    imageNameIndex5++;

                    i_name = "a" + imageNameIndex5;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 270 && rowIndex <= 315) {

                    imageNameIndex2++;

                    i_name = "n" + imageNameIndex2;

                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 316 && rowIndex <= 342) {

                    imageNameIndex3++;

                    i_name = "n" + imageNameIndex3;

                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 343) {

                    imageNameIndex1++;

                    i_name = "n" + imageNameIndex1;

                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else {
                    items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                }
            }
            fileInputStream.close();

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycleView);
        searchView = view.findViewById(R.id.search_bar);
        loadData();

        viewAdapter = new ViewAdapter(getContext(), items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(viewAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                findData(newText);
                return true;
            }
        });
    }

    public void onResume() {
        super.onResume();
    }

    void findData(String name) {
        List<Item> list = new ArrayList<>();
        for(Item data : items) {
            if(data.getName().toLowerCase().contains(name.toLowerCase())) {
                list.add(data);
            }
        }
        viewAdapter.updateList(list);
    }
}