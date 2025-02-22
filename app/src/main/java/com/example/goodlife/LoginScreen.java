package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Objects;

public class LoginScreen extends AppCompatActivity {

    private EditText editTextName, editTextPassword;
    private Button loginButton;
    private String signInDate;
    private String filename = "Storage.txt";
    File myInternalFile;
    private String filepath = "Super_mystery_folder";
    String username_tmp, password_tmp, islogin = "false";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Layout sceen
        super.onCreate(savedInstanceState);

        // Make status bar fully transparent
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));

        // Set the layout to extend into the status bar
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        ContextWrapper contextWrapper = new ContextWrapper(
                getApplicationContext());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // check login
        File directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);
        myInternalFile = new File(directory, filename);
        if(check_is_login()){
            checkUserData(username_tmp, password_tmp);
        } else{
            Log.d("wtf","wtf");
        }

        setContentView(R.layout.activity_login);


        // Set value for object zone
        editTextName = findViewById(R.id.editEditTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);
        signInDate = getTodaysDate();

        // Login zone
        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validDataUsername() || !validDataUserPassword()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Name", String.valueOf(editTextName.getText()));
                    editor.putString("SignInDate", signInDate);
                    editor.apply();
                    String name = String.valueOf(editTextName.getText()).trim();
                    String password = String.valueOf(editTextPassword.getText()).trim();
                    checkUserData(name, password);
                }
            }
        });
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,RegisterPage.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    // use to get Date
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    // Convert to string day
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + "/" + day + "/" + year;
    }

    // Format month from number to String
    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }

    // Check if UserName is correct
    public Boolean validDataUsername() {
        String name;
        name = String.valueOf(editTextName.getText());
        if(name.isEmpty()) {
            editTextName.setError("Vui lòng nhập vào tên người dùng!");
            return false;
        } else {
            editTextName.setError(null);
            return true;
        }
    }
    // Check is Password correct
    public Boolean validDataUserPassword() {
        String password;
        password = String.valueOf(editTextPassword.getText());
        if(password.isEmpty()) {
            editTextName.setError("Vui lòng nhập vào mật khẩu người dùng!");
            return false;
        } else {
            editTextName.setError(null);
            return true;
        }
    }
    // Login function
    public void checkUserData(String name, String password) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(name);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    editTextName.setError(null);
                    String passwordFromDB = snapshot.child(name).child("password").getValue(String.class);

                    if(Objects.equals(passwordFromDB, password)) {
                        editTextName.setError(null);
                        Toast.makeText(LoginScreen.this, "Đăng nhập tài khoản thành công!", Toast.LENGTH_SHORT).show();
                        try {
                            String data = name + "\n" + password + "\n" +"true";
                            FileOutputStream fos = new FileOutputStream(myInternalFile);
                            fos.write(data.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(LoginScreen.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        editTextPassword.setError("Mật khẩu không đúng!");
                        editTextPassword.requestFocus();
                    }
                } else {
                    editTextName.setError("Tài khoản không tồn tại!");
                    editTextName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean check_is_login(){
        try {
            FileInputStream fis = new FileInputStream(myInternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in));
            String strLine ;
            int i = 1;
            while ((strLine = br.readLine()) != null) {
                if(i==1) username_tmp = strLine;
                if(i==2) password_tmp = strLine;
                if(i==3) islogin = strLine;
                i++;
            }
            in.close();
            if(islogin.equals("true")) {
                return true;
            } else return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}