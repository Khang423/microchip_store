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
import com.example.microchip.activity.orderDetail.ListViewProductActivity;
import com.example.microchip.activity.product.EditProductActivity;
import com.example.microchip.db.OrderDetailHelper;
import com.example.microchip.db.ProductHelper;
import com.example.microchip.model.Product;

import java.util.List;

public class ListViewProductAdapter extends RecyclerView.Adapter<ListViewProductAdapter.CustomerViewHolder> {

    private ProductHelper dbHelper;
    private Context mContext;
    private List<Product> listProduct;
    private int order_id;
    public ListViewProductAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Product> list, int order_id) {
        this.listProduct = list;
        this.order_id = order_id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view_product, parent, false);
        return new CustomerViewHolder(view);
    }

    // ham set du lieu cho adapter
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Product product = listProduct.get(position);
        if (product == null) {
            return;
        }
        // kiểm tra xem url img có giá trị không
        String urlImg = product.getUrl_img();
        if (urlImg != null) {
            holder.imgProduct.setImageURI(Uri.parse(urlImg));
        } else {
            holder.imgProduct.setImageResource(R.drawable.anh1); // Hình ảnh mặc định nếu không có
        }
        holder.tv_name_product.setText(product.getName());
        holder.tv_price_product.setText(String.valueOf(product.getPrice()));
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int product_id = product.getId();
                double price = product.getPrice();

                OrderDetailHelper orderDetailHelper = new OrderDetailHelper(mContext);
                orderDetailHelper.add(order_id,product_id,price,1);
                Intent resultIntent = new Intent();
                ((Activity) mContext).setResult(-1, resultIntent);
                ((Activity) mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listProduct != null) {
            return listProduct.size();
        }
        return 0;
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btn_add;
        TextView tv_name_product, tv_price_product;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.img_product);
            tv_name_product = itemView.findViewById(R.id.tv_name_product);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
            btn_add = itemView.findViewById(R.id.btn_add);
        }
    }

}
