package com.example.microchip.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.R;
import com.example.microchip.activity.orderDetail.OrderDetailActivity;
import com.example.microchip.activity.orderDetail.ViewOrderDetailActivity;
import com.example.microchip.activity.orderDetail.ViewOrderDetailPendingApprovalActivity;
import com.example.microchip.db.OrderHelper;
import com.example.microchip.fragment.InvoiceFragment;
import com.example.microchip.model.Order;

import java.util.List;

public class HasOrderedAdapter extends RecyclerView.Adapter<HasOrderedAdapter.OrderViewHolder> {

    private OrderHelper dbHelper;
    private Context mContext;
    private List<Order> mListOrder;

    public HasOrderedAdapter(Context mContext) {
        this.mContext = mContext;
    }
    private LoadDataLister listener;
    public void setData(List<Order> list) {
        this.mListOrder = list;
        notifyDataSetChanged();
    }
    public void LoadDataLister(LoadDataLister listener) {
        this.listener = listener;
    }
    public interface LoadDataLister {
        void loadData();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_has_ordered, parent, false);
        return new OrderViewHolder(view);
    }

    // ham set du lieu cho adapter
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order item = mListOrder.get(position);
        if(item.getStatus() == 2 ){
            holder.order_status.setText("Chờ duyệt");
            holder.order_status.setBackgroundResource(R.drawable.custom_status1);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ViewOrderDetailPendingApprovalActivity.class);
                    intent.putExtra("order_id",item.getId());
                    ((Activity) mContext).startActivityForResult(intent,100);
                }
            });
        }else{
            holder.order_status.setText("Đã duyệt");
            holder.order_status.setBackgroundResource(R.drawable.custom_status);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ViewOrderDetailActivity.class);
                    intent.putExtra("order_id",item.getId());
                    ((Activity) mContext).startActivityForResult(intent,100);
                }
            });
        }
        holder.tv_order.setText("Hoá đơn " + item.getId());

        holder.order_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderHelper orderHelper = new OrderHelper(mContext);
                orderHelper.updateStatus(item.getId());

                Toast.makeText(mContext,"Chuyển sang trạng thái đã duyệt thành công.",Toast.LENGTH_SHORT).show();
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
        TextView tv_order,order_status;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrder = itemView.findViewById(R.id.img_order);
            tv_order = itemView.findViewById(R.id.tv_name_order);
            order_status = itemView.findViewById(R.id.order_status);
        }
    }
}
