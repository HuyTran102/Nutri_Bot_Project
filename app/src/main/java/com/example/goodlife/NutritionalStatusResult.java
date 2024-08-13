package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.Objects;

public class NutritionalStatusResult extends AppCompatActivity {

    Button backButton;

    String name, signInDate, gender, password, date, height, weight;

    private static final String TAG = "ExcelRead";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritional_status_result);

        backButton = findViewById(R.id.back_button);

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("Name",null);
        signInDate = sharedPreferences.getString("SignInDate", null);
        height = sharedPreferences.getString("Height", null);
        weight = sharedPreferences.getString("Weight", null);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query userDatabase = reference.orderByChild("name").equalTo(name);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gender = snapshot.child(name).child("gender").getValue(String.class);
                password = snapshot.child(name).child("password").getValue(String.class);
                date = snapshot.child(name).child("date_of_birth").getValue(String.class);

                int monthAge = calculateMonthAge();

                double BMI = calculateBMI();

                String firstRowCell = null;

                try {
                    InputStream inputStream = getAssets().open("bmiBoys.xlsx");

                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet sheet = workbook.getSheetAt(0);

                    Row firstRow = sheet.getRow(0);
                    Cell firstCell = firstRow.getCell(0);

                    firstRowCell = firstCell.getStringCellValue();

//                    Log.d(TAG, firstCell.getStringCellValue());

                    workbook.close();
                    inputStream.close();
                } catch (Exception e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }

                Toast.makeText(NutritionalStatusResult.this, " " + firstRowCell + " ", Toast.LENGTH_SHORT).show();

//                Toast.makeText(NutritionalStatusResult.this, String.valueOf(BMI), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionalStatusResult.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    String getNumberMonthFormat(String month) {
        if (Objects.equals(month, "JAN"))
            return "1";
        if (Objects.equals(month, "FEB"))
            return "2";
        if (Objects.equals(month, "MAR"))
            return "3";
        if (Objects.equals(month, "APR"))
            return "4";
        if (Objects.equals(month, "MAY"))
            return "5";
        if (Objects.equals(month, "JUN"))
            return "6";
        if (Objects.equals(month, "JUL"))
            return "7";
        if (Objects.equals(month, "AUG"))
            return "8";
        if (Objects.equals(month, "SEP"))
            return "9";
        if (Objects.equals(month, "OCT"))
            return "10";
        if (Objects.equals(month, "NOV"))
            return "11";
        if (Objects.equals(month, "DEC"))
            return "11";

        return "1";
    }

    int calculateMonthAge() {
        String tempSignInDate = signInDate;
        String tempDateOfBirth = date;

        String[] signIn = tempSignInDate.split("/");
        String[] birth = tempDateOfBirth.split("/");

        int signInMonth = Integer.parseInt(getNumberMonthFormat(signIn[0]));
        int signInDay = Integer.parseInt(signIn[1]);
        int signInYear = Integer.parseInt(signIn[2]);

        int birthMonth = Integer.parseInt(getNumberMonthFormat(birth[0]));
        int birthDay = Integer.parseInt(birth[1]);
        int birthYear = Integer.parseInt(birth[2]);

        int yearDifferent = signInYear - birthYear;
        int monthDifferent = signInMonth - birthMonth;

        int monthAge = yearDifferent * 12 + monthDifferent;

        if(signInDay < birthDay) {
            monthAge -= 1;
        }

        return monthAge;
    }

    double calculateBMI() {
        double userHeight, userWeight;

        userHeight = Double.parseDouble(height);
        userWeight = Double.parseDouble(weight);;

        return userWeight / (userHeight * userHeight);
    }
}