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
import com.example.microchip.db.OrderDetailHelper;
import com.example.microchip.db.OrderHelper;
import com.example.microchip.model.Order;
import com.example.microchip.model.OrderDetail;
import com.example.microchip.model.Product;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private OrderDetailHelper dbHelper;
    private Context mContext;
    private List<OrderDetail> mListOrderDetail;

    public OrderDetailAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<OrderDetail> list) {
        this.mListOrderDetail = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    // ham set du lieu cho adapter
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail item = mListOrderDetail.get(position);
        dbHelper = new OrderDetailHelper(mContext);
        Product product = dbHelper.getInfo(item.getProduct_id());

        holder.imgOrderDetail.setImageURI(Uri.parse(product.getUrl_img())); // Hình ảnh mặc định nếu không có
        holder.tv_order.setText(product.getName());
        holder.tv_price.setText(String.valueOf(product.getPrice()));
        holder.tv_quantity.setText(String.valueOf(item.getQuantity()));
        holder.ic_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.quantityUp(item.getOrder_id(),product.getId());
                item.setQuantity(item.getQuantity() + 1);
                notifyItemChanged(position);
                Intent resultIntent = new Intent();
                ((Activity) mContext).setResult(-1, resultIntent);
            }
        });
        holder.ic_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.quantityDown(item.getOrder_id(),product.getId());
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    notifyItemChanged(position);
                } else {
                    mListOrderDetail.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mListOrderDetail.size());
                }
                Intent resultIntent = new Intent();
                ((Activity) mContext).setResult(-1, resultIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListOrderDetail != null) {
            return mListOrderDetail.size();
        }
        return 0;
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOrderDetail, ic_down,ic_up;
        TextView tv_order,tv_price,tv_total,tv_quantity;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrderDetail = itemView.findViewById(R.id.img_order_detail);
            tv_order = itemView.findViewById(R.id.tv_name_product);
            tv_price = itemView.findViewById(R.id.tv_price_product);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            ic_up = itemView.findViewById(R.id.ic_up);
            ic_down = itemView.findViewById(R.id.ic_down);
        }
    }
}
