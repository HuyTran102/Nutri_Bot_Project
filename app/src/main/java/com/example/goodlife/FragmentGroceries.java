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

public class FragmentGroceries extends Fragment {
    private RecyclerView recyclerView;
    private List<Item> items = new ArrayList<>();
    private SearchView searchView;
    private ViewAdapter viewAdapter;
    private static final String TAG = "ExcelRead";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_groceries, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycleView);
        searchView = view.findViewById(R.id.search_bar);

        String path = "Diary.xlsx";

        try {

            AssetManager am = getContext().getAssets();
            InputStream fileInputStream = am.open(path);

            int imageNameIndex1 = 1000, imageNameIndex2 = 2000, imageNameIndex3 = 3000
                    , imageNameIndex4 = 4000, imageNameIndex5 = 5000, imageNameIndex6 = 6000
                    , imageNameIndex7 = 7000, imageNameIndex8 = 8000, imageNameIndex9 = 9000
                    , imageNameIndex10 = 10000, imageNameIndex11 = 11000, imageNameIndex12 = 12000
                    , imageNameIndex13 = 13000, imageNameIndex14 = 14000;

            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
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

                if(rowIndex >= 0 && rowIndex <= 22) {

                    imageNameIndex1++;

                    i_name = "a" + imageNameIndex1;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 23 && rowIndex <= 48) {

                    imageNameIndex2++;

                    i_name = "a" + imageNameIndex2;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 49 && rowIndex <= 87) {

                    imageNameIndex3++;

                    i_name = "a" + imageNameIndex3;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 88 && rowIndex <= 218) {

                    imageNameIndex4++;

                    i_name = "a" + imageNameIndex4;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 219 && rowIndex <= 274) {

                    imageNameIndex5++;

                    i_name = "a" + imageNameIndex5;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 275 && rowIndex <= 291) {

                    imageNameIndex6++;

                    i_name = "a" + imageNameIndex6;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 292 && rowIndex <= 384) {

                    imageNameIndex7++;

                    i_name = "a" + imageNameIndex7;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 385 && rowIndex <= 451) {

                    imageNameIndex8++;

                    i_name = "a" + imageNameIndex8;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 452 && rowIndex <= 462) {

                    imageNameIndex9++;

                    i_name = "a" + imageNameIndex9;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 463 && rowIndex <= 471) {

                    imageNameIndex10++;

                    i_name = "a" + imageNameIndex10;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 472 && rowIndex <= 492) {

                    imageNameIndex11++;

                    i_name = "a" + imageNameIndex11;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 493 && rowIndex <= 519) {

                    imageNameIndex12++;

                    i_name = "a" + imageNameIndex12;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 520 && rowIndex <= 542) {

                    imageNameIndex13++;

                    i_name = "a" + imageNameIndex13;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                } else if(rowIndex >= 543) {

                    imageNameIndex14++;

                    i_name = "a" + imageNameIndex14;
                    if(this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()) == 0) {
                        items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
                    } else {
                        items.add(new Item(String.valueOf(name), this.getResources().getIdentifier(i_name, "drawable", getActivity().getPackageName()), kcal, protein, lipid, glucid, unit_type));
                    }
                }

//                items.add(new Item(String.valueOf(name), R.drawable.noimageavailable, kcal, protein, lipid, glucid, unit_type));
            }
            fileInputStream.close();

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }

        viewAdapter = new ViewAdapter(getContext(), items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(viewAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                findData(query);
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