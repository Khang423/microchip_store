package com.example.microchip.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.R;
import com.example.microchip.activity.product.EditProductActivity;
import com.example.microchip.activity.productType.EditProductTypeActivity;
import com.example.microchip.db.ProductTypeHelper;
import com.example.microchip.model.ProductType;

import java.util.List;

public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.CustomerViewHolder> {

    private ProductTypeHelper productTypeHelper;
    private Context mContext;
    private List<ProductType> listProductType;

    public ProductTypeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ProductType> list) {
        this.listProductType = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_type, parent, false);
        return new CustomerViewHolder(view);
    }

    // ham set du lieu cho adapter
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        ProductType productType = listProductType.get(position);
        if (productType == null) {
            return;
        }
        holder.tv_name_product.setText(productType.getName());

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditProductTypeActivity.class);
                intent.putExtra("id", productType.getId());
                intent.putExtra("name", productType.getName());
                ((Activity) mContext).startActivityForResult(intent, 100);
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productTypeHelper = new ProductTypeHelper(mContext);
                productTypeHelper.deleteProductType(productType.getId());

                listProductType.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listProductType.size());

                Toast.makeText(mContext, "Xoá thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listProductType != null) {
            return listProductType.size();
        }
        return 0;
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btn_delete, btn_edit;
        TextView tv_name_product, tv_price_product;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.img_product);
            tv_name_product = itemView.findViewById(R.id.tv_name_product);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
            btn_edit = itemView.findViewById(R.id.btn_edit);
        }
    }

}
