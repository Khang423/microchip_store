package com.example.microchip.activity.order;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.adapter.HasOrderedAdapter;
import com.example.microchip.adapter.OrderAdapter;
import com.example.microchip.db.OrderHelper;
import com.example.microchip.model.Order;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HasOrderedActivity extends AppCompatActivity {

    public static final int RESULT_PRODUCT_ACTIVITY = 1;
    SQLiteDatabase db;

    private RecyclerView rcvOrder;
    private HasOrderedAdapter hasOrderedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_ordered);

        rcvOrder = findViewById(R.id.rcv_order);
        hasOrderedAdapter = new HasOrderedAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvOrder.setLayoutManager(linearLayoutManager);

        hasOrderedAdapter.setData(getListOrder());
        rcvOrder.setAdapter(hasOrderedAdapter);
    }

    private List<Order> getListOrder() {
        List<Order> list = new ArrayList<>();
        db = openOrCreateDatabase("microchip.db", MODE_PRIVATE, null);
        int customer_id = GlobalSession.getSession().getId();
        Cursor cursor = db.rawQuery("select * from [order] where customer_id = ? and status = 2 ", new String[]{String.valueOf(customer_id)});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(0),//id
                        cursor.getInt(1),//name
                        cursor.getDouble(2),//email
                        cursor.getInt(3),//tel
                        cursor.getString(4),//url avatar
                        cursor.getString(5)
                );
                list.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Load lại dữ liệu
            hasOrderedAdapter.setData(getListOrder());
            hasOrderedAdapter.notifyDataSetChanged();
        }
    }
}