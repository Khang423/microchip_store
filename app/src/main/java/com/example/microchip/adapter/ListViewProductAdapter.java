package com.example.microchip.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.R;
import com.example.microchip.model.Product;
import com.example.microchip.model.ProductData;
import com.example.microchip.model.SharedViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListViewProductAdapter extends RecyclerView.Adapter<ListViewProductAdapter.CustomerViewHolder> {

    private Context mContext;
    private List<Product> listProduct;
    private int order_id;
    private SharedViewModel sharedViewModel;

    // 🔹 Truyền FragmentActivity vào adapter để sử dụng ViewModel
    public ListViewProductAdapter(FragmentActivity activity, Context mContext) {
        this.mContext = mContext;
        this.sharedViewModel = new ViewModelProvider(activity).get(SharedViewModel.class);
    }

    public void setData(List<Product> list, int order_id) {
        this.listProduct = list;
        this.order_id = order_id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_product, parent, false);
        return new CustomerViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Product product = listProduct.get(position);
        if (product == null) {
            return;
        }
        setUpUI(product, holder);

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductData productData = new ProductData(
                        product.getId(),
                        product.getPrice(),
                        1,
                        product.getName(), // Mặc định số lượng = 1, có thể cập nhật sau
                        product.getUrl_img()
                );
                sharedViewModel.addProductToOrder(productData);
                Toast.makeText(mContext,"Thêm vào giỏ hành thành công",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (listProduct != null) ? listProduct.size() : 0;
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
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

    public void setUpUI(Product product, CustomerViewHolder holder) {
        String urlImg = product.getUrl_img();

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormat.format(product.getPrice());

        if (urlImg != null) {
            holder.imgProduct.setImageURI(Uri.parse(urlImg));
        } else {
            holder.imgProduct.setImageResource(R.drawable.anh1);
        }
        holder.tv_name_product.setText(product.getName());
        holder.tv_price_product.setText("Giá : " + formattedPrice );
    }
}
