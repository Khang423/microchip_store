package com.example.microchip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_login;
    TextView btn_register;
    EditText username, password;

    SQLiteDatabase db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
//        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    void init()
    {
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
//            Intent intent = new Intent(this, RegisterActivity.class);
//            startActivity(intent);
            String adminName = "admin";
            String adminPassword = "admin123";

            String inputUsername = username.getText().toString();
            String inputPassword = password.getText().toString();

            if (adminName.equals(inputUsername) && adminPassword.equals(inputPassword)) {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}