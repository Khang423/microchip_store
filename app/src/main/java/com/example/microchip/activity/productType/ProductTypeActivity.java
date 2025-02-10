package com.example.microchip.activity.productType;

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
import com.example.microchip.adapter.ProductTypeAdapter;
import com.example.microchip.model.ProductType;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeActivity extends AppCompatActivity {

    ImageView btnAdd;

    SQLiteDatabase db;

    RecyclerView rcvProductType;
    ProductTypeAdapter productTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type);
        init();

        productTypeAdapter = new ProductTypeAdapter(this);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcvProductType.setLayoutManager(linearLayoutManager);

        productTypeAdapter.setData(getListProduct());
        rcvProductType.setAdapter(productTypeAdapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductTypeActivity.this, AddProductTypeActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    public void init() {
        btnAdd = findViewById(R.id.btn_add);
        rcvProductType = findViewById(R.id.rcv_product_type);
    }

    private List<ProductType> getListProduct(){
        List<ProductType> list = new ArrayList<>();
        db = openOrCreateDatabase("microchip.db",MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("select * from product_type", null);

        if(cursor.moveToFirst()){
            do {
                ProductType productType = new ProductType(
                        cursor.getInt(0),//id
                        cursor.getString(1)
                );
                list.add(productType);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Load lại dữ liệu
            productTypeAdapter.setData(getListProduct());
            productTypeAdapter.notifyDataSetChanged();
        }
    }
}