package com.example.microchip.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.R;
import com.example.microchip.activity.product.AddProductActivity;
import com.example.microchip.activity.product.ProductActivity;
import com.example.microchip.activity.productType.ProductTypeActivity;
import com.example.microchip.adapter.ListViewProductAdapter;
import com.example.microchip.model.Product;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    private View view;
    private ImageView btnAdd;
    private SQLiteDatabase db;
    private RecyclerView rcvProduct;
    private ListViewProductAdapter lvproductAdapter;
    private int order_id;
    MaterialToolbar materialToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product, container, false);
        init(view);

        if (getArguments() != null) {
            order_id = getArguments().getInt("order_id", -1);
        }

        lvproductAdapter = new ListViewProductAdapter(requireActivity() ,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvProduct.setLayoutManager(linearLayoutManager);

        lvproductAdapter.setData(getListProduct(), order_id);
        rcvProduct.setAdapter(lvproductAdapter);

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_product) {
                    Intent intent = new Intent(getActivity(), ProductActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.product_type) {
                    Intent intent = new Intent(getActivity(), ProductTypeActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        return view;
    }

    private void init(View view) {
        btnAdd = view.findViewById(R.id.btn_add);
        rcvProduct = view.findViewById(R.id.rcv_product);
        materialToolbar = view.findViewById(R.id.toolbar);
    }

    private List<Product> getListProduct() {
        List<Product> list = new ArrayList<>();
        db = getActivity().openOrCreateDatabase("microchip.db", getActivity().MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from product where status = 0" , null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getString(5),
                        cursor.getString(6), cursor.getInt(7), cursor.getInt(8),
                        cursor.getInt(9), cursor.getInt(10), cursor.getInt(11),
                        cursor.getInt(12), cursor.getString(13), cursor.getDouble(14)
                );
                list.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        lvproductAdapter.setData(getListProduct(), order_id);
        lvproductAdapter.notifyDataSetChanged();
    }

}
