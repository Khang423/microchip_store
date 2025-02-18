package com.example.microchip.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.adapter.HasOrderedAdapter;
import com.example.microchip.model.Order;

import java.util.ArrayList;
import java.util.List;

public class InvoiceFragment extends Fragment {

    private SQLiteDatabase db;
    private RecyclerView rcvOrder;
    private HasOrderedAdapter hasOrderedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);
        init(view);

        hasOrderedAdapter = new HasOrderedAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvOrder.setLayoutManager(linearLayoutManager);

        hasOrderedAdapter.setData(getListOrder());
        rcvOrder.setAdapter(hasOrderedAdapter);

        return view;
    }

    private List<Order> getListOrder() {
        List<Order> list = new ArrayList<>();
        db = requireContext().openOrCreateDatabase("microchip.db", getContext().MODE_PRIVATE, null);
        int customer_id = GlobalSession.getSession().getId();
        Cursor cursor = db.rawQuery("SELECT * FROM [order] WHERE customer_id = ? AND status = 2", new String[]{String.valueOf(customer_id)});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(0), // id
                        cursor.getInt(1), // name
                        cursor.getDouble(2), // email
                        cursor.getInt(3), // tel
                        cursor.getString(4), // url avatar
                        cursor.getString(5) // status
                );
                list.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private void init(View view) {
        rcvOrder = view.findViewById(R.id.rcv_order);
    }

    @Override
    public void onResume() {
        super.onResume();
        hasOrderedAdapter.setData(getListOrder());
        hasOrderedAdapter.notifyDataSetChanged();
    }
}
