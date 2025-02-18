package com.example.microchip.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.R;
import com.example.microchip.activity.orderDetail.OrderDetailActivity;
import com.example.microchip.db.CustomerHelper;
import com.example.microchip.db.OrderHelper;
import com.example.microchip.model.Customer;
import com.example.microchip.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private OrderHelper dbHelper;
    private Context mContext;
    private List<Order> mListOrder;

    public OrderAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Order> list) {
        this.mListOrder = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    // ham set du lieu cho adapter
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order item = mListOrder.get(position);

        holder.tv_order.setText("Hoá đơn " +item.getId());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View iew) {
                dbHelper = new OrderHelper(mContext);
                dbHelper.deleteOrder(item.getId());
                mListOrder.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(mContext, "Xoá thành công", Toast.LENGTH_SHORT).show(); // Thông báo
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("order_id",item.getId());
                ((Activity) mContext).startActivityForResult(intent,100);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOrder, btn_delete;
        TextView tv_order;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrder = itemView.findViewById(R.id.img_order);
            tv_order = itemView.findViewById(R.id.tv_name_order);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
