package com.example.microchip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    public static final int RESULT_PRODUCT_ACTIVITY = 1;
    SQLiteDatabase db;

    private RecyclerView rcvCustomer;
    private CustomerAdapter customerAdapter;
    private CircleImageView btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();

        rcvCustomer = findViewById(R.id.rcv_customer);
        customerAdapter = new CustomerAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcvCustomer.setLayoutManager(linearLayoutManager);

        customerAdapter.setData(getListCusTomer());
        rcvCustomer.setAdapter(customerAdapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this,AddAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<Customer> getListCusTomer() {
        List<Customer> list = new ArrayList<>();
        db = openOrCreateDatabase("microchip.db",MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("select * from customer", null);

        if(cursor.moveToFirst()){
            do {
                Customer customer = new Customer(
                        cursor.getInt(0),//id
                        cursor.getString(1),//name
                        cursor.getString(2),//email
                        cursor.getString(3),//tel
                        cursor.getString(4),//url avatar
                        cursor.getInt(5),//gender
                        cursor.getString(6),//birthday
                        cursor.getString(7),//password
                        cursor.getString(8)//address
                );
                list.add(customer);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void init(){
        btn_add = findViewById(R.id.btn_add);
    }
}