package com.example.microchip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

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
    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer cus = mlistCustomer.get(position);
        if(cus == null){
            return;
        }
        holder.imgUser.setImageResource(cus.getId());
        holder.tv_user.setText(cus.getName());
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
