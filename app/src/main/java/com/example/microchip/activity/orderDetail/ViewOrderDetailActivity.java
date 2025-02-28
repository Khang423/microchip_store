package com.example.microchip.activity.orderDetail;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.adapter.OrderDetailAdapter;
import com.example.microchip.adapter.ViewOrderDetailAdapter;
import com.example.microchip.db.OrderDetailHelper;
import com.example.microchip.db.OrderHelper;
import com.example.microchip.model.OrderDetail;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewOrderDetailActivity extends AppCompatActivity {

    public static final int RESULT_PRODUCT_ACTIVITY = 1;
    SQLiteDatabase db;
    OrderDetailHelper orderDetailHelper;
    private RecyclerView rcvOrderDetail;
    private ViewOrderDetailAdapter viewOrderDetailAdapter;
    TextView tv_total;
    TextInputLayout textInputLayoutAddress, textInputLayoutCustomerName;
    TextInputEditText input_address, input_customer_name,input_created_at;
    int order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_detail);
        init();
        // lấy order từ intent
        order_id = getIntent().getIntExtra("order_id", -1);

        viewOrderDetailAdapter = new ViewOrderDetailAdapter(this);
        String name = GlobalSession.getSession().getName();
        orderDetailHelper = new OrderDetailHelper(this);

        double total_price = orderDetailHelper.totalPrice(order_id);
        String created_at = orderDetailHelper.getCreatedAt(order_id);
        String address = orderDetailHelper.getAddress(order_id);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormat.format(total_price);

        tv_total.setText("Tổng tiền: " + formattedPrice);

        input_address.setText(address);
        input_customer_name.setText(name);
        input_address.setEnabled(false);
        input_customer_name.setEnabled(false);
        input_created_at.setText(created_at);
        input_created_at.setEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvOrderDetail.setLayoutManager(linearLayoutManager);

        viewOrderDetailAdapter.setData(getListOrderDetail());
        rcvOrderDetail.setAdapter(viewOrderDetailAdapter);

    }

    private List<OrderDetail> getListOrderDetail() {
        List<OrderDetail> list = new ArrayList<>();
        db = openOrCreateDatabase("microchip.db", MODE_PRIVATE, null);
        int order_id = getIntent().getIntExtra("order_id", -1);
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
        tv_total = findViewById(R.id.tv_total);
        rcvOrderDetail = findViewById(R.id.rcv_order_detail);
        input_address = findViewById(R.id.input_address);
        textInputLayoutAddress = findViewById(R.id.textInputLayoutAddress);
        input_customer_name = findViewById(R.id.input_customer_name);
        textInputLayoutCustomerName = findViewById(R.id.textInputLayoutCustomerName);
        input_created_at = findViewById(R.id.input_created_at);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Load lại dữ liệu
            viewOrderDetailAdapter.setData(getListOrderDetail());
            viewOrderDetailAdapter.notifyDataSetChanged();
        }
    }
}