package com.example.microchip.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.activity.DashboardActivity;
import com.example.microchip.activity.auth.LoginActivity;
import com.example.microchip.activity.customer.EditCustomerActivity;
import com.example.microchip.db.AuthHelper;
import com.example.microchip.db.CustomerHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

     Button btn_logout,btn_profile,btn_delete;
    CircleImageView imageReview;
     TextInputEditText inputName, inputTel, inputMail, inputPassword, inputGender, inputBirthday, inputAddress;
    Uri selectedImageUri;
     SQLiteDatabase db;
    TextView tv_customer_mail,tv_customer_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        setUpData();
        onResume();
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthHelper authHelper =  new AuthHelper(getActivity());
                authHelper.logOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(), EditCustomerActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int customer_id = GlobalSession.getSession().getId();
                CustomerHelper customerHelper =  new CustomerHelper(getActivity());
                customerHelper.deleteCustomer(customer_id);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return view;
    }


    private void init(View view) {
        imageReview = view.findViewById(R.id.imageReview);
        inputName = view.findViewById(R.id.input_name);
        inputMail = view.findViewById(R.id.input_mail);
        tv_customer_mail = view.findViewById(R.id.tv_customer_mail);
        tv_customer_name = view.findViewById(R.id.tv_customer_name);
        btn_profile = view.findViewById(R.id.btn_profile);
        btn_delete = view.findViewById(R.id.btn_delete);
        btn_logout = view.findViewById(R.id.btn_logout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            String newData = data.getStringExtra("data_key");
            // Cập nhật dữ liệu mới
            String img_url = GlobalSession.getSession().getUrl_avatar();
            String name = GlobalSession.getSession().getName();
            String email = GlobalSession.getSession().getEmail();

            if(img_url != null) {
                imageReview.setImageURI(Uri.parse(img_url));
            }else{
                imageReview.setImageResource(R.drawable.anh1);
            }

            tv_customer_mail.setText(email);
            tv_customer_name.setText(name);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        setUpData();  // Hàm load lại dữ liệu
    }
    public void setUpData() {
        String img_url = GlobalSession.getSession().getUrl_avatar();
        String name = GlobalSession.getSession().getName();
        String email = GlobalSession.getSession().getEmail();

        if(img_url != null) {
            imageReview.setImageURI(Uri.parse(img_url));
        }else{
            imageReview.setImageResource(R.drawable.anh1);
        }

        tv_customer_mail.setText(email);
        tv_customer_name.setText(name);

    }
}
