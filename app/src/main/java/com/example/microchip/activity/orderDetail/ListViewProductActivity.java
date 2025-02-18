package com.example.microchip.activity.orderDetail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.R;
import com.example.microchip.activity.product.AddProductActivity;
import com.example.microchip.adapter.ListViewProductAdapter;
import com.example.microchip.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ListViewProductActivity extends AppCompatActivity {

    ImageView btnAdd;

    SQLiteDatabase db;

    RecyclerView rcvProduct;
    ListViewProductAdapter lvproductAdapter;
    int order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_product);
        init();
        order_id= getIntent().getIntExtra("order_id",-1);
//        lvproductAdapter = new ListViewProductAdapter(g,this);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcvProduct.setLayoutManager(linearLayoutManager);

        lvproductAdapter.setData(getListProduct(),order_id);
        rcvProduct.setAdapter(lvproductAdapter);
    }

    public void init() {
        btnAdd = findViewById(R.id.btn_add);
        rcvProduct = findViewById(R.id.rcv_product);
    }

    private List<Product> getListProduct(){
        List<Product> list = new ArrayList<>();
        db = openOrCreateDatabase("microchip.db",MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("select * from product", null);

        if(cursor.moveToFirst()){
            do {
                Product product = new Product(
                        cursor.getInt(0),//id
                        cursor.getString(1),//name
                        cursor.getString(2),//email
                        cursor.getString(3),//tel
                        cursor.getInt(4),//url avatar
                        cursor.getString(5),//gender
                        cursor.getString(6),//birthday
                        cursor.getInt(7),//password
                        cursor.getInt(8),//address
                        cursor.getInt(9),//address
                        cursor.getInt(10),//address
                        cursor.getInt(11),//address
                        cursor.getInt(12),//address
                        cursor.getString(13),//address
                        cursor.getDouble(14)
                );
                list.add(product);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Load lại dữ liệu
            lvproductAdapter.setData(getListProduct(),order_id);
            lvproductAdapter.notifyDataSetChanged();
        }
    }
}