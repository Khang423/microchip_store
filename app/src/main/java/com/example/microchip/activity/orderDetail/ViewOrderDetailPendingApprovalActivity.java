package com.example.microchip.activity.orderDetail;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.adapter.OrderDetailAdapter;
import com.example.microchip.adapter.ViewOrderDetailAdapter;
import com.example.microchip.adapter.ViewOrderDetailPendignApprovalAdapter;
import com.example.microchip.db.OrderDetailHelper;
import com.example.microchip.db.OrderHelper;
import com.example.microchip.model.OrderDetail;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewOrderDetailPendingApprovalActivity extends AppCompatActivity {

    public static final int RESULT_PRODUCT_ACTIVITY = 1;
    SQLiteDatabase db;
    OrderDetailHelper orderDetailHelper;
    private RecyclerView rcvOrderDetail;
    private ViewOrderDetailPendignApprovalAdapter adapter;
    TextView tv_total;
    TextInputLayout textInputLayoutAddress, textInputLayoutCustomerName;
    TextInputEditText input_address, input_customer_name, input_created_at;
    Button btn_update;
    int order_id;
    MaterialToolbar materialToolbar;
    List<OrderDetail> orderDetailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_pending_approval);
        init();
        setUpValue();
        // lấy order từ intent
        order_id = getIntent().getIntExtra("order_id", -1);
        adapter = new ViewOrderDetailPendignApprovalAdapter(this);
        orderDetailHelper = new OrderDetailHelper(this);
        double total_price = orderDetailHelper.totalPrice(order_id);
        String created_at = orderDetailHelper.getCreatedAt(order_id);
        String address = orderDetailHelper.getAddress(order_id);
        String name = GlobalSession.getSession().getName();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormat.format(total_price);
        tv_total.setText("Tổng tiền: " + formattedPrice);
        input_address.setText(address);
        input_customer_name.setText(name);
        input_customer_name.setEnabled(false);
        input_created_at.setText(created_at);
        input_created_at.setEnabled(false);

        OrderHelper orderHelper = new OrderHelper(ViewOrderDetailPendingApprovalActivity.this);

        adapter.setOnQuantityChangeListener(new OrderDetailAdapter.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged() {
                updateTotalPrice();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvOrderDetail.setLayoutManager(linearLayoutManager);

        adapter.setData(getListOrderDetail());
        rcvOrderDetail.setAdapter(adapter);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double total_price = orderDetailHelper.totalPrice(order_id);
                String address = input_address.getText().toString();

                orderHelper.updateOrder(order_id, address, total_price);
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete){
                    orderHelper.deleteOrder(order_id);
                    finish();
                }
                return false;
            }
        });
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
        btn_update = findViewById(R.id.btn_update);
        materialToolbar = findViewById(R.id.toolbar);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Load lại dữ liệu
            adapter.setData(getListOrderDetail());
            adapter.notifyDataSetChanged();
        }
    }

    private void update() {
        OrderDetailHelper orderDetailHelper = new OrderDetailHelper(this);
        OrderHelper orderHelper = new OrderHelper(this);
        int customer_id = GlobalSession.getSession().getId();
        String address = input_address.getText().toString().trim();

        // thời gian
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedTime = now.format(formatter);

        orderHelper.addOrder(customer_id, address, formattedTime);
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        android.icu.text.NumberFormat currencyFormat = android.icu.text.NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (OrderDetail item : orderDetailList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        String formattedPrice = currencyFormat.format(totalPrice);
        tv_total.setText("Tổng tiền: " + formattedPrice);
        recreate();
    }

    public void setUpValue() {

    }
}