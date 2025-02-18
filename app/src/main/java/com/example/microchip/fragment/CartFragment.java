package com.example.microchip.fragment;

import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.adapter.OrderDetailAdapter;
import com.example.microchip.db.OrderDetailHelper;
import com.example.microchip.db.OrderHelper;
import com.example.microchip.model.OrderDetail;
import com.example.microchip.model.ProductData;
import com.example.microchip.model.SharedViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartFragment extends Fragment {

    private RecyclerView rcvOrder;
    private OrderDetailAdapter orderDetailAdapter;
    private SharedViewModel sharedViewModel;
    private MaterialToolbar toolbarCart;
    TextView tv_total;
    Button btn_order;

    List<OrderDetail> orderDetailList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        init(view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Thiết lập Adapter cho RecyclerView
        orderDetailAdapter = new OrderDetailAdapter(getContext());
        orderDetailAdapter.setOnQuantityChangeListener(new OrderDetailAdapter.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged() {
                updateTotalPrice();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvOrder.setLayoutManager(linearLayoutManager);
        rcvOrder.setAdapter(orderDetailAdapter);

        sharedViewModel.getOrderList().observe(getViewLifecycleOwner(), new Observer<List<ProductData>>() {
            @Override
            public void onChanged(List<ProductData> productData) {
                if (productData != null) {
                    Log.d("CartFragment", "Cập nhật giỏ hàng: " + productData.size() + " sản phẩm");

                    Map<Integer, OrderDetail> orderDetailMap = new HashMap<>();

                    for (ProductData item : productData) {
                        int productId = item.getId();

                        if (orderDetailMap.containsKey(productId)) {
                            OrderDetail existingItem = orderDetailMap.get(productId);
                            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                        } else {
                            OrderDetail orderDetail = new OrderDetail(
                                    productId,         // product_id
                                    item.getId(), // product_name
                                    item.getId(),      // img_url
                                    item.getPrice(),       // price
                                    item.getQuantity()     // quantity
                            );
                            orderDetailMap.put(productId, orderDetail);
                        }
                    }

                    rcvOrder.postDelayed(() -> updateTotalPrice(), 100);

                    orderDetailList.clear();
                    orderDetailList.addAll(orderDetailMap.values());

                    orderDetailAdapter.setData(orderDetailList);
                    orderDetailAdapter.notifyDataSetChanged();
                }
            }
        });

        btn_order.setOnClickListener(view1->saveOrderToDatabase());

        return view;
    }

    public void init(View view) {
        rcvOrder = view.findViewById(R.id.rcv_order);
        toolbarCart = view.findViewById(R.id.toolbar);
        tv_total = view.findViewById(R.id.tv_total);
        btn_order = view.findViewById(R.id.btn_order);
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (OrderDetail item : orderDetailList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        String formattedPrice = currencyFormat.format(totalPrice);
        tv_total.setText("Tổng tiền: " + formattedPrice );
    }

    private void saveOrderToDatabase() {
        OrderDetailHelper orderDetailHelper = new OrderDetailHelper(getActivity());
        OrderHelper orderHelper = new OrderHelper(getActivity());
        int customer_id = GlobalSession.getSession().getId();
        String address = GlobalSession.getSession().getAddress();
        // thời gian
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedTime = now.format(formatter);

        orderHelper.addOrder(customer_id,address,formattedTime);
        if (orderDetailList.isEmpty()) {
            Toast.makeText(getContext(), "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (OrderDetail item : orderDetailList) {
            orderDetailHelper.add(item);
        }
        Toast.makeText(getContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();

        // Xóa giỏ hàng sau khi lưu
        sharedViewModel.clearCart();
        orderDetailList.clear();
        orderDetailAdapter.setData(orderDetailList);
        updateTotalPrice();
    }
}
