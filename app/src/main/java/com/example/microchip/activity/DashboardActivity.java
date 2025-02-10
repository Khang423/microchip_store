package com.example.microchip.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.microchip.R;
import com.example.microchip.activity.customer.CustomerActivity;
import com.example.microchip.activity.product.ProductActivity;
import com.example.microchip.activity.productType.ProductTypeActivity;

public class DashboardActivity extends AppCompatActivity {
    Button btn_account, btn_product, btn_product_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        btn_account.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, CustomerActivity.class);
                startActivity(intent);
            }
        });
        btn_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
        btn_product_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ProductTypeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init() {
        btn_account = findViewById(R.id.btn_account);
        btn_product = findViewById(R.id.btn_product);
        btn_product_type = findViewById(R.id.btn_product_type);
    }
}