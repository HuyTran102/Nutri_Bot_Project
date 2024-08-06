package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{

    TextInputEditText editTextName, editTextPassword;

    Button signIn;

    LinearLayout signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.acc_name);
        editTextPassword = findViewById(R.id.acc_password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);

        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterPage.class);
            startActivity(intent);
            finish();
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validDataUsername() || !validDataUserPassword())
                {
                    checkUserData();
                }
            }
        });

    }

    public Boolean validDataUsername() {
        String name;
        name = String.valueOf(editTextName.getText());
        if(name.isEmpty())
        {
            editTextName.setError("Vui lòng nhập vào tên người dùng!");
            return false;
        } else
        {
            editTextName.setError(null);
            return true;
        }
    }

    public Boolean validDataUserPassword() {
        String password;
        password = String.valueOf(editTextPassword.getText());
        if(password.isEmpty())
        {
            editTextName.setError("Vui lòng nhập vào mật khẩu người dùng!");
            return false;
        } else
        {
            editTextName.setError(null);
            return true;
        }
    }
    public void checkUserData()
    {
        String name, password;
        name = String.valueOf(editTextName.getText()).trim();
        password = String.valueOf(editTextPassword.getText()).trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(name);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if(snapshot.exists())
                {
                    editTextName.setError(null);
                    String passwordFromDB = snapshot.child(name).child("password").getValue(String.class);

                    if(Objects.equals(passwordFromDB, password))
                    {
                        editTextName.setError(null);
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                    } else
                    {
                        editTextPassword.setError("Mật khẩu không đúng!");
                        editTextPassword.requestFocus();
                    }
                } else
                {
                    editTextName.setError("Tài khoản không tồn tại!");
                    editTextName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}