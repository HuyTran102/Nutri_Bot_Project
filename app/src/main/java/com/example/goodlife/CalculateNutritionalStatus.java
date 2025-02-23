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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CalculateNutritionalStatus extends AppCompatActivity {
    private Button backButton, resultButton;
    private TextInputEditText userHeight, userWeight;
    private String name, signInDate, gender, password, date, height, weight;
    private TextView bmiStatusView, hfaStatusView, heightView, weightView;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static final String TAG = "ExcelRead";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_nutritional_status);

        // Make status bar fully transparent
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));

        // Set the layout to extend into the status bar
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        backButton = findViewById(R.id.back_button);
        resultButton = findViewById(R.id.result_button);
        userHeight = findViewById(R.id.user_height);
        userWeight = findViewById(R.id.user_weight);
        backButton = findViewById(R.id.back_button);
        bmiStatusView = findViewById(R.id.bmiView);
        hfaStatusView = findViewById(R.id.hfaView);
        heightView = findViewById(R.id.height_view);
        weightView = findViewById(R.id.weight_view);

        final double[] userRecommendWeight = new double[1];
        final double[] userRecommendHeight = new double[1];
        final double[] userActualWeight = new double[1];
        final double[] userActualHeight = new double[1];

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("Name", null);
        signInDate = sharedPreferences.getString("SignInDate", null);

        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validUserHeight() && validUserWeight()) {
                    height = String.valueOf(userHeight.getText());
                    weight = String.valueOf(userWeight.getText());
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
                    Query userDatabase = reference.orderByChild("name").equalTo(name);

                    userActualHeight[0] = Double.parseDouble(height);
                    userActualWeight[0] = Double.parseDouble(weight);

                    userDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            gender = snapshot.child(name).child("gender").getValue(String.class);
                            password = snapshot.child(name).child("password").getValue(String.class);
                            date = snapshot.child(name).child("date_of_birth").getValue(String.class);
//
//                            Toast.makeText(CalculateNutritionalStatus.this, " " + gender + " ", Toast.LENGTH_SHORT).show();

                            int monthAge = calculateMonthAge();

                            if(monthAge >= 120 && monthAge <= 228) {

                                double BMI = calculateBMI();

                                userRecommendWeight[0] = bmiStatusWarning(gender, BMI, monthAge);

                                userRecommendHeight[0] = heightForAgeStatusWarning(gender, Double.parseDouble(height), monthAge);

                                WriteDataFireBase(String.valueOf(userActualHeight[0]), String.valueOf(userActualWeight[0])
                                        , String.valueOf(userRecommendHeight[0]), String.valueOf(userRecommendWeight[0]));
                            } else {
                                Toast.makeText(CalculateNutritionalStatus.this, "Ngày tháng năm sinh không hợp lệ!", Toast.LENGTH_SHORT).show();
                                
                                bmiStatusView.setText("null");
                                hfaStatusView.setText("null");
                                heightView.setText("null");
                                weightView.setText("null");

                                WriteDataFireBase(String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1));
                            }
                            
//                            Toast.makeText(CalculateNutritionalStatus.this, " " + monthAge, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalculateNutritionalStatus.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Write Data to Cloud Firestone
    public void WriteDataFireBase(String userHeight, String userWeight, String userRecommendHeight, String userRecommendWeight){
        // Create a new item with all of the value
        Map<String, Object> item = new HashMap<>();
        item.put("userHeight", userHeight);
        item.put("userWeight", userWeight);
        item.put("userRecommendHeight", userRecommendHeight);
        item.put("userRecommendWeight", userRecommendWeight);

        firebaseFirestore.collection("GoodLife")
                .document(name).collection("Dinh dưỡng")
                .document("Value")
                .set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Adding value to database successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error adding value to database: ", e);
                    }
                });
    }

    public Boolean validUserHeight() {
        String height;
        height = String.valueOf(userHeight.getText());
        if(height.isEmpty()) {
            userHeight.setError("Vui lòng nhập vào chiều cao người dùng!");
            return false;
        } else {
            userHeight.setError(null);
            return true;
        }
    }

    public Boolean validUserWeight() {
        String weight;
        weight = String.valueOf(userWeight.getText());
        if(weight.isEmpty()) {
            userWeight.setError("Vui lòng nhập vào cân nặng người dùng!");
            return false;
        } else {
            userWeight.setError(null);
            return true;
        }
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

    double bmiStatusWarning(String gender, double bmi, int monthAge) {
        String path;
        if(gender.equals("Nam")) {
            path = "bmiBoys.xlsx";
        } else if(gender.equals("Nu")) {
            path = "bmiGirls.xlsx";
        } else {
            Toast.makeText(CalculateNutritionalStatus.this, "Không thể cảnh báo tình trạng BMI do giới tính không hợp lệ !", Toast.LENGTH_SHORT).show();
            return 0;
        }

        try {
            AssetManager am = getAssets();
            InputStream is = am.open(path);

            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            double userRecommendWeight = 0;
            for(int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex ++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(0);
                int value = (int) cell.getNumericCellValue();
                if(value == monthAge) {
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
                        bmiStatusView.setTextColor(getResources().getColor(R.color.large));
                        bmiStatusView.setText("  Béo phì !");
                    } else if(positiveSD2 <= bmi && bmi <= positiveSD3) {
                        bmiStatusView.setTextColor(getResources().getColor(R.color.large));
                        bmiStatusView.setText("  Béo phì !");
                    } else if(positiveSD1 <= bmi && bmi <= positiveSD2) {
                        bmiStatusView.setTextColor(getResources().getColor(R.color.large));
                        bmiStatusView.setText("  Thừa cân !");
                    } else if(negativeSD2 <= bmi && bmi <= positiveSD1) {
                        bmiStatusView.setTextColor(getResources().getColor(R.color.normal));
                        bmiStatusView.setText("  Bình thường !");
                    } else if(negativeSD3 <= bmi && bmi <= negativeSD2) {
                        bmiStatusView.setTextColor(getResources().getColor(R.color.small));
                        bmiStatusView.setText("  Gầy còm vừa !");
                    } else if(bmi < negativeSD3){
                        bmiStatusView.setTextColor(getResources().getColor(R.color.small));
                        bmiStatusView.setText("  Gầy còm nặng !");
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("0.0");

                    String result = "";

                    if(bmi < positiveSD3 && bmi > positiveSD1) {
                        result += "Thừa ";

                        double recommendWeight = positiveSD1 * Double.parseDouble(height) * Double.parseDouble(height);

                        userRecommendWeight = recommendWeight;

                        double subtrac = Double.parseDouble(weight) - recommendWeight;

                        if(subtrac == 0) {
                            result += "Bình thường!";

                            userRecommendWeight = Double.parseDouble(this.weight);
                            weightView.setTextColor(getResources().getColor(R.color.normal));
                            weightView.setText(result);
                        } else {
                            result += decimalFormat.format(subtrac) + " (kg)";
                            weightView.setTextColor(getResources().getColor(R.color.large));
                            weightView.setText(result);
                        }
                    }  else if(negativeSD2 <= bmi && bmi <= positiveSD1) {
                        result += "Bình thường!";

                        userRecommendWeight = Double.parseDouble(this.weight);
                        weightView.setTextColor(getResources().getColor(R.color.normal));
                        weightView.setText(result);
                    } else {
                        result += "Thiếu ";

                        double recommendWeight = negativeSD1 * Double.parseDouble(height) * Double.parseDouble(height);

                        userRecommendWeight = recommendWeight;

                        double add = Math.abs(Double.parseDouble(weight) - recommendWeight);

                        result += decimalFormat.format(add) + " (kg)";
                        weightView.setTextColor(getResources().getColor(R.color.small));
                        weightView.setText(result);
                    }
                }
            }

            workbook.close();

            return userRecommendWeight;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return 0;
    }

    double heightForAgeStatusWarning(String gender, double height, int monthAge) {
        height *= 100;
        String path;
        if(gender.equals("Nam")) {
            path = "hfaBoys.xlsx";
        } else if(gender.equals("Nu")) {
            path = "hfaGirls.xlsx";
        } else {
            Toast.makeText(CalculateNutritionalStatus.this, "Không thể cảnh báo tình trạng BMI do giới tính không hợp lệ !", Toast.LENGTH_SHORT).show();
            return 0;
        }

        try {
            AssetManager am = getAssets();
            InputStream is = am.open(path);

            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            double userRecommendHeight = 0;
            for(int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex ++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(0);
                int value = (int) cell.getNumericCellValue();
                if(value == monthAge) {
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
                        hfaStatusView.setTextColor(getResources().getColor(R.color.large));
                        hfaStatusView.setText("  Béo phì !");
                    } else if(positiveSD2 <= height && height <= positiveSD3) {
                        hfaStatusView.setTextColor(getResources().getColor(R.color.normal));
                        hfaStatusView.setText("  Bình thường !");
                    } else if(positiveSD1 <= height && height <= positiveSD2) {
                        hfaStatusView.setTextColor(getResources().getColor(R.color.normal));
                        hfaStatusView.setText("  Bình thường !");
                    } else if(negativeSD2 <= height && height <= positiveSD1) {
                        hfaStatusView.setTextColor(getResources().getColor(R.color.normal));
                        hfaStatusView.setText("  Bình thường !");
                    } else if(negativeSD3 <= height && height <= negativeSD2) {
                        hfaStatusView.setTextColor(getResources().getColor(R.color.small));
                        hfaStatusView.setText("  Thấp còi vừa !");
                    } else if(height < negativeSD3){
                        hfaStatusView.setTextColor(getResources().getColor(R.color.small));
                        hfaStatusView.setText("  Thấp còi nặng !");
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("0.0");

                    String result = "";

                    if(height > positiveSD3 && height >= positiveSD1) {
                        result += "Thừa ";

                        double subtrac = height - positiveSD1;

                        userRecommendHeight = positiveSD1;

                        result += decimalFormat.format(subtrac) + " (cm)";
                        heightView.setTextColor(getResources().getColor(R.color.large));
                        heightView.setText(result);
                    }  else if(negativeSD2 <= height && height <= positiveSD3) {
                        result += "Bình thường!";

                        userRecommendHeight = height;
                        heightView.setTextColor(getResources().getColor(R.color.normal));
                        heightView.setText(result);
                    } else {
                        result += "Thiếu ";

                        double add = Math.abs(height - negativeSD1);

                        userRecommendHeight = negativeSD1;

                        result += decimalFormat.format(add) + " (cm)";
                        heightView.setTextColor(getResources().getColor(R.color.small));
                        heightView.setText(result);
                    }
                }
            }

            workbook.close();

            return userRecommendHeight;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return 0;
    }

}