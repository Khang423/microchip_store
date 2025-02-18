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

import com.example.microchip.db.DatabaseHelper;
import com.example.microchip.activity.product.EditProductActivity;
import com.example.microchip.R;
import com.example.microchip.db.ProductHelper;
import com.example.microchip.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CustomerViewHolder> {

    private ProductHelper dbHelper;
    private Context mContext;
    private List<Product> listProduct;

    public ProductAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Product> list) {
        this.listProduct = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
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
        holder.tv_price_product.setText("Giá tiền : "+product.getPrice());

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditProductActivity.class);
                intent.putExtra("id", product.getId());
                intent.putExtra("name", product.getName());
                intent.putExtra("img", product.getUrl_img());
                intent.putExtra("cpu", product.getCpu());
                intent.putExtra("clock_speed", product.getClock_speed());
                intent.putExtra("flash_size", product.getFlash_size());
                intent.putExtra("psram_size", product.getPram_size());
                intent.putExtra("wifi_sp", product.getWifi_sp());
                intent.putExtra("bt_sp", product.getBt_sp());
                intent.putExtra("gpio_count", product.getGpio_channels());
                intent.putExtra("adc_channel", product.getAdc_channels());
                intent.putExtra("dac_channel", product.getDac_chanels());
                intent.putExtra("product_type_id", product.getProduct_type_id());
                intent.putExtra("brand", product.getBrand());
                intent.putExtra("price", product.getPrice());
                ((Activity) mContext).startActivityForResult(intent, 100);

            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper = new ProductHelper(mContext);
                dbHelper.deleteProduct(product.getId());

                listProduct.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listProduct.size());

                Toast.makeText(mContext, "Xoá thành công", Toast.LENGTH_SHORT).show();
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
