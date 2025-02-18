package com.example.microchip.activity.order;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.activity.customer.AddCustomerActivity;
import com.example.microchip.adapter.CustomerAdapter;
import com.example.microchip.adapter.OrderAdapter;
import com.example.microchip.db.OrderHelper;
import com.example.microchip.model.Customer;
import com.example.microchip.model.Order;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderActivity extends AppCompatActivity {

    public static final int RESULT_PRODUCT_ACTIVITY = 1;
    SQLiteDatabase db;

    private RecyclerView rcvOrder;
    private OrderAdapter orderAdapter;
    private ImageView btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        init();

        rcvOrder = findViewById(R.id.rcv_order);
        orderAdapter = new OrderAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvOrder.setLayoutManager(linearLayoutManager);

        orderAdapter.setData(getListOrder());
        rcvOrder.setAdapter(orderAdapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int customer_id = GlobalSession.getSession().getId();
                String address = GlobalSession.getSession().getAddress();
                double total = 0.0;
                int status = 1;
                ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                String formattedTime = now.format(formatter);
                OrderHelper orderHelper = new OrderHelper(OrderActivity.this);
//                orderHelper.addOrder(customer_id,total,status,formattedTime,address);
                orderAdapter.setData(getListOrder());
                orderAdapter.notifyDataSetChanged();
            }
        });
    }

    private List<Order> getListOrder() {
        List<Order> list = new ArrayList<>();
        db = openOrCreateDatabase("microchip.db", MODE_PRIVATE, null);
        int customer_id = GlobalSession.getSession().getId();
        Cursor cursor = db.rawQuery("select * from [order] where customer_id = ? and status = 1 ", new String[]{String.valueOf(customer_id)});

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

    public void init() {
        btn_add = findViewById(R.id.btn_add);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Load lại dữ liệu
            orderAdapter.setData(getListOrder());
            orderAdapter.notifyDataSetChanged();
        }
    }
}