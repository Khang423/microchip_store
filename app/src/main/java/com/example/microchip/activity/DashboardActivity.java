package com.example.microchip.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.activity.customer.CustomerActivity;
import com.example.microchip.activity.order.HasOrderedActivity;
import com.example.microchip.activity.order.OrderActivity;
import com.example.microchip.activity.product.ProductActivity;
import com.example.microchip.activity.productType.ProductTypeActivity;

public class DashboardActivity extends AppCompatActivity {
    Button btn_account, btn_product, btn_product_type,btn_order,btn_has_ordered,btn_statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        getValueFromSession();
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
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        btn_has_ordered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, HasOrderedActivity.class);
                startActivity(intent);
            }
        });
        btn_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, StatisticAcitivity.class);
                startActivity(intent);
            }
        });
    }
    public void getValueFromSession(){
        int id = GlobalSession.getSession().getId();
        String tel = GlobalSession.getSession().getTel();
        String name = GlobalSession.getSession().getName();
        String email = GlobalSession.getSession().getEmail();
        String url = GlobalSession.getSession().getUrl_avatar();
        int gender = GlobalSession.getSession().getGender();
        String birthday = GlobalSession.getSession().getBirthday();
        String password = GlobalSession.getSession().getPassword();
        String address = GlobalSession.getSession().getAddress();
    }
    public void init() {
        btn_account = findViewById(R.id.btn_account);
        btn_product = findViewById(R.id.btn_product);
        btn_product_type = findViewById(R.id.btn_product_type);
        btn_order = findViewById(R.id.btn_order);
        btn_has_ordered = findViewById(R.id.btn_has_ordered);
        btn_statistics = findViewById(R.id.btn_statistics);
    }
}