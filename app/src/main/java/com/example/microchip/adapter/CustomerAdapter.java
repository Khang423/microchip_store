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
import androidx.recyclerview.widget.RecyclerView;

import com.example.microchip.db.CustomerHelper;
import com.example.microchip.db.DatabaseHelper;
import com.example.microchip.R;
import com.example.microchip.model.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private CustomerHelper dbHelper;
    private Context mContext;
    private List<Customer> mlistCustomer;

    public CustomerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Customer> list) {
        this.mlistCustomer = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer,parent,false);
        return new CustomerViewHolder(view);
    }

    // ham set du lieu cho adapter
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer cus = mlistCustomer.get(position);
        if(cus == null){
            return;
        }
        String urlAvatar = cus.getUrl_avatar();
        if (urlAvatar != null) {
            holder.imgUser.setImageURI(Uri.parse(urlAvatar));
        } else {
            holder.imgUser.setImageResource(R.drawable.anh1); // Hình ảnh mặc định nếu không có
        }
        holder.tv_user.setText(cus.getName());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper = new CustomerHelper(mContext);
                dbHelper.deleteCustomer(cus.getId());
                mlistCustomer.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(mContext, "Xoá thành công",Toast.LENGTH_SHORT).show(); // Thông báo
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mlistCustomer != null) {
            return mlistCustomer.size();
        }
        return 0;
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser, btn_delete;
        TextView tv_user;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.img_user);
            tv_user = itemView.findViewById(R.id.tv_user);
            btn_delete =  itemView.findViewById(R.id.btn_delete);


        }
    }
}
