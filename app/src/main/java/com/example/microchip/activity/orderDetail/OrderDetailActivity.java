package com.example.microchip.activity.orderDetail;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.R;
import com.example.microchip.adapter.OrderDetailAdapter;
import com.example.microchip.db.OrderDetailHelper;
import com.example.microchip.model.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    public static final int RESULT_PRODUCT_ACTIVITY = 1;
    SQLiteDatabase db;
    OrderDetailHelper orderDetailHelper;
    private RecyclerView rcvOrderDetail;
    private OrderDetailAdapter orderDetailAdapter;
    private ImageView btn_add;
    TextView title,tv_total;
    int order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();

        order_id = getIntent().getIntExtra("order_id",-1);


        orderDetailAdapter = new OrderDetailAdapter(this);
        orderDetailHelper = new OrderDetailHelper(this);
        double total_price = orderDetailHelper.totalPrice(order_id);
        title.setText("CHI TIẾT HOÁ ĐƠN HĐ" + order_id);
        tv_total.setText("Tổng tiền : " + total_price);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvOrderDetail.setLayoutManager(linearLayoutManager);

        orderDetailAdapter.setData(getListOrderDetail());
        rcvOrderDetail.setAdapter(orderDetailAdapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order_id = getIntent().getIntExtra("order_id",-1);
                Intent intent = new Intent(OrderDetailActivity.this, ListViewProductActivity.class);
                intent.putExtra("order_id",order_id);
                startActivityForResult(intent,100);
            }
        });
    }

    private List<OrderDetail> getListOrderDetail() {
        List<OrderDetail> list = new ArrayList<>();
        db = openOrCreateDatabase("microchip.db", MODE_PRIVATE, null);
        int order_id = getIntent().getIntExtra("order_id",-1);
        Cursor cursor = db.rawQuery("select * from [order_detail] where [order_id] = ?", new String[]{String.valueOf(order_id)});

        if (cursor.moveToFirst()) {
            do {
                OrderDetail orderDetail = new OrderDetail(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getDouble(3),
                        cursor.getInt(4)
                );
                list.add(orderDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void init() {
        btn_add = findViewById(R.id.btn_add);
        title = findViewById(R.id.title);
        tv_total = findViewById(R.id.tv_total);
        rcvOrderDetail = findViewById(R.id.rcv_order_detail);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Load lại dữ liệu
            orderDetailAdapter.setData(getListOrderDetail());
            orderDetailAdapter.notifyDataSetChanged();
            double total_price = orderDetailHelper.totalPrice(order_id);
            tv_total.setText("Tổng tiền : " + total_price);
        }
    }
}