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
import android.widget.TextView;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Objects;

public class NutritionalStatusResult extends AppCompatActivity {

    Button backButton;

    String name, signInDate, gender, password, date, height, weight;

    TextView bmiStatusView, hfaStatusView, heightView, weightView;

    private static final String TAG = "ExcelRead";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritional_status_result);

        backButton = findViewById(R.id.back_button);
        bmiStatusView = findViewById(R.id.bmiView);
        hfaStatusView = findViewById(R.id.hfaView);
        heightView = findViewById(R.id.height_view);
        weightView = findViewById(R.id.weight_view);

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

                bmiStatusWarning(gender, BMI, monthAge);

                heightForAgeStatusWarning(gender, Double.parseDouble(height), monthAge);
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
        userWeight = Double.parseDouble(weight);

        return userWeight / (userHeight * userHeight);
    }
    
    void bmiStatusWarning(String gender, double bmi, int monthAge) {
        String path;
        if(gender.equals("nam")) {
            path = "bmiBoys.xlsx";
        } else if(gender.equals("nữ")) {
            path = "bmiGirls.xlsx";
        } else {
            Toast.makeText(NutritionalStatusResult.this, "Không thể cảnh báo tình trạng BMI do giới tính không hợp lệ !", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            AssetManager am = getAssets();
            InputStream is = am.open(path);

            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            for(int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex ++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(0);
                int value = (int) cell.getNumericCellValue();
                if(value == monthAge) {
//                    Toast.makeText(NutritionalStatusResult.this, "Đã tìm thấy giá trị tháng tuổi !", Toast.LENGTH_SHORT).show();
                    cell = row.getCell(1);
                    double negativeSD3 = (double) cell.getNumericCellValue();
                    cell = row.getCell(2);
                    double negativeSD2 = (double) cell.getNumericCellValue();
                    cell = row.getCell(3);
                    double negativeSD1 = (double) cell.getNumericCellValue();
                    cell = row.getCell(4);
                    double positiveSD0 = (double) cell.getNumericCellValue();
                    cell = row.getCell(5);
                    double positiveSD1 = (double) cell.getNumericCellValue();
                    cell = row.getCell(6);
                    double positiveSD2 = (double) cell.getNumericCellValue();
                    cell = row.getCell(7);
                    double positiveSD3 = (double) cell.getNumericCellValue();

                    if(bmi > positiveSD3) {
                        bmiStatusView.setText("  Béo phì !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng BMI: Béo phì !", Toast.LENGTH_SHORT).show();
                    } else if(positiveSD2 <= bmi && bmi <= positiveSD3) {
                        bmiStatusView.setText("  Béo phì !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng BMI: Béo phì !", Toast.LENGTH_SHORT).show();
                    } else if(positiveSD1 <= bmi && bmi <= positiveSD2) {
                        bmiStatusView.setText("  Thừa cân !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng BMI: Thừa cân !", Toast.LENGTH_SHORT).show();
                    } else if(negativeSD2 <= bmi && bmi <= positiveSD1) {
                        bmiStatusView.setText("  Bình thường !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng BMI: Bình thường !", Toast.LENGTH_SHORT).show();
                    } else if(negativeSD3 <= bmi && bmi <= negativeSD2) {
                        bmiStatusView.setText("  Gầy còm vừa !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng BMI: Gầy còm vừa !", Toast.LENGTH_SHORT).show();
                    } else if(bmi < negativeSD3){
                        bmiStatusView.setText("  Gầy còm nặng !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng BMI: Gầy còm nặng !", Toast.LENGTH_SHORT).show();
                    }

//                    Toast.makeText(NutritionalStatusResult.this, " " + negativeSD3 + " " + negativeSD2 + " " + negativeSD1 + " " + positiveSD0 + " " + positiveSD1 + " " + positiveSD2 + " " + positiveSD3 + " ", Toast.LENGTH_SHORT).show();
                    
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");

                    String result = "";

                    if(bmi < positiveSD3 && bmi >= positiveSD1) {
                        result += "Thừa ";

                        double recommendWeight = positiveSD1 * Double.parseDouble(height) * Double.parseDouble(height);

                        double subtrac = Double.parseDouble(weight) - recommendWeight;

                        result += decimalFormat.format(subtrac) + " (kg)";
                        weightView.setText(result);
                    }  else if(negativeSD2 <= bmi && bmi <= positiveSD1) {
                        result += "Bình thường 0.0 (kg)";
                    } else {
                        result += "Thiếu ";

                        double recommendWeight = negativeSD1 * Double.parseDouble(height) * Double.parseDouble(height);

                        double add = Math.abs(Double.parseDouble(weight) - recommendWeight);

                        result += decimalFormat.format(add) + " (kg)";
                        weightView.setText(result);
                    }
                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    void heightForAgeStatusWarning(String gender, double height, int monthAge) {
        height *= 100;
        String path;
        if(gender.equals("nam")) {
            path = "hfaBoys.xlsx";
        } else if(gender.equals("nữ")) {
            path = "hfaGirls.xlsx";
        } else {
            Toast.makeText(NutritionalStatusResult.this, "Không thể cảnh báo tình trạng BMI do giới tính không hợp lệ !", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            AssetManager am = getAssets();
            InputStream is = am.open(path);

            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            for(int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex ++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(0);
                int value = (int) cell.getNumericCellValue();
                if(value == monthAge) {
//                    Toast.makeText(NutritionalStatusResult.this, "Đã tìm thấy giá trị tháng tuổi !", Toast.LENGTH_SHORT).show();
                    cell = row.getCell(1);
                    double negativeSD3 = (double) cell.getNumericCellValue();
                    cell = row.getCell(2);
                    double negativeSD2 = (double) cell.getNumericCellValue();
                    cell = row.getCell(3);
                    double negativeSD1 = (double) cell.getNumericCellValue();
                    cell = row.getCell(4);
                    double positiveSD0 = (double) cell.getNumericCellValue();
                    cell = row.getCell(5);
                    double positiveSD1 = (double) cell.getNumericCellValue();
                    cell = row.getCell(6);
                    double positiveSD2 = (double) cell.getNumericCellValue();
                    cell = row.getCell(7);
                    double positiveSD3 = (double) cell.getNumericCellValue();

                    if(height > positiveSD3) {
                        hfaStatusView.setText("  Béo phì !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng chiều cao theo tuổi: Béo phì !", Toast.LENGTH_SHORT).show();
                    } else if(positiveSD2 <= height && height <= positiveSD3) {
                        hfaStatusView.setText("  Bình thường !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng chiều cao theo tuổi: Bình thường !", Toast.LENGTH_SHORT).show();
                    } else if(positiveSD1 <= height && height <= positiveSD2) {
                        hfaStatusView.setText("  Bình thường !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng chiều cao theo tuổi: Bình thường !", Toast.LENGTH_SHORT).show();
                    } else if(negativeSD2 <= height && height <= positiveSD1) {
                        hfaStatusView.setText("  Bình thường !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng chiều cao theo tuổi: Bình thường !", Toast.LENGTH_SHORT).show();
                    } else if(negativeSD3 <= height && height <= negativeSD2) {
                        hfaStatusView.setText("  Thấp còi vừa !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng chiều cao theo tuổi: Thấp còi vừa !", Toast.LENGTH_SHORT).show();
                    } else if(height < negativeSD3){
                        hfaStatusView.setText("  Thấp còi nặng !");
//                        Toast.makeText(NutritionalStatusResult.this, "Tình trạng chiều cao theo tuổi: Thấp còi nặng !", Toast.LENGTH_SHORT).show();
                    }

//                    Toast.makeText(NutritionalStatusResult.this, " " + negativeSD3 + " " + negativeSD2 + " " + negativeSD1 + " " + positiveSD0 + " " + positiveSD1 + " " + positiveSD2 + " " + positiveSD3 + " ", Toast.LENGTH_SHORT).show();

                    DecimalFormat decimalFormat = new DecimalFormat("0.0");

                    String result = "";

                    if(height < positiveSD3 && height >= positiveSD1) {
                        result += "Thừa ";

                        double subtrac = height - positiveSD1;

                        result += decimalFormat.format(subtrac) + " (cm)";
                        heightView.setText(result);
                    }  else if(negativeSD2 <= height && height <= negativeSD2) {
                        result += "Bình thường 0.0 (cm)";
                    } else {
                        result += "Thiếu ";

                        double add = Math.abs(height - negativeSD1);

                        result += decimalFormat.format(add) + " (cm)";
                        heightView.setText(result);
                    }
                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }
}