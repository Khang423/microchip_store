package com.example.microchip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login extends AppCompatActivity implements View.OnClickListener{
    Button btn_login;
    TextView btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btn_register){
            Intent intent = new Intent(this,register.class);
            startActivity(intent);
        }
    }

    void init()
    {
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
    }


}