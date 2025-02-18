package com.example.microchip.activity.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.microchip.R;
import com.example.microchip.adapter.ProductAdapter;
import com.example.microchip.model.Product;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
        SQLiteDatabase db;

    RecyclerView rcvProduct;
    ProductAdapter productAdapter;
    MaterialToolbar materialToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        init();

        productAdapter = new ProductAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvProduct.setLayoutManager(linearLayoutManager);

        productAdapter.setData(getListProduct());
        rcvProduct.setAdapter(productAdapter);

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_product) {
                    Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
                    startActivityForResult(intent, 100);
                }
                return false;
            }
        });
    }

    public void init() {
        rcvProduct = findViewById(R.id.rcv_product);
        materialToolbar = findViewById(R.id.toolbar);
    }

    private List<Product> getListProduct() {
        List<Product> list = new ArrayList<>();
        db = openOrCreateDatabase("microchip.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from product where status=0", null);

        if (cursor.moveToFirst()) {
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
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Load lại dữ liệu
            productAdapter.setData(getListProduct());
            productAdapter.notifyDataSetChanged();
        }
    }
}