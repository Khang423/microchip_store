package com.example.microchip.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.activity.StatisticAcitivity;
import com.example.microchip.activity.auth.LoginActivity;
import com.example.microchip.activity.customer.CustomerActivity;
import com.example.microchip.activity.order.HasOrderedActivity;
import com.example.microchip.activity.order.OrderActivity;
import com.example.microchip.activity.product.ProductActivity;
import com.example.microchip.activity.productType.ProductTypeActivity;
import com.example.microchip.db.AuthHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    Button btn_account, btn_product, btn_product_type,btn_order,btn_has_ordered,btn_statistics,btn_logout;
    TextView tv_name;
    CircleImageView img_avatar;
     View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        init(view);
        setUp();
        return view;

    }

    public void init(View view) {
        btn_account = view.findViewById(R.id.btn_account);
        btn_product = view.findViewById(R.id.btn_product);
        btn_product_type = view.findViewById(R.id.btn_product_type);
        btn_order = view.findViewById(R.id.btn_order);
        btn_has_ordered = view.findViewById(R.id.btn_has_ordered);
        btn_statistics = view.findViewById(R.id.btn_statistics);
        btn_logout = view.findViewById(R.id.btn_logout);
        img_avatar = view.findViewById(R.id.img_avatar);
        tv_name = view.findViewById(R.id.tv_name);
    }

    public void setUp(){
        String img_url = GlobalSession.getSession().getUrl_avatar();
        String name = GlobalSession.getSession().getName();

        tv_name.setText(name);
        if (img_url == null) {
            img_avatar.setImageResource(R.drawable.anh1);
        } else {
            img_avatar.setImageURI(Uri.parse(img_url));
        }

        // Xử lý sự kiện khi nhấn nút
        btn_account.setOnClickListener(view -> startActivity(new Intent(getActivity(), CustomerActivity.class)));
        btn_product.setOnClickListener(view -> startActivity(new Intent(getActivity(), ProductActivity.class)));
        btn_product_type.setOnClickListener(view -> startActivity(new Intent(getActivity(), ProductTypeActivity.class)));
        btn_order.setOnClickListener(view -> startActivity(new Intent(getActivity(), OrderActivity.class)));
        btn_has_ordered.setOnClickListener(view -> startActivity(new Intent(getActivity(), HasOrderedActivity.class)));
        btn_statistics.setOnClickListener(view -> startActivity(new Intent(getActivity(), StatisticAcitivity.class)));

        btn_logout.setOnClickListener(view -> {
            AuthHelper authHelper = new AuthHelper(requireContext());
            authHelper.logOut();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}
