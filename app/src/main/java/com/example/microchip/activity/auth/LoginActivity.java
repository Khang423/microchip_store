package com.example.microchip.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.microchip.GlobalSession;
import com.example.microchip.activity.DashboardActivity;
import com.example.microchip.db.AuthHelper;
import com.example.microchip.db.CustomerHelper;
import com.example.microchip.db.DatabaseHelper;
import com.example.microchip.R;
import com.example.microchip.model.Customer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    TextView btn_register;
    TextInputEditText edt_mail, edt_password;
    TextInputLayout layout_email, layout_pass;
    TextView err_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String username = edt_mail.getText().toString();
                String password = edt_password.getText().toString();

                validateField(layout_email,username);
                validateField(layout_pass,password);

                if (!(username.isEmpty()) && !(password.isEmpty())) {
                    AuthHelper dbHelper = new AuthHelper(LoginActivity.this);
                    if (dbHelper.checkLoginCustomer(username, password)) {
                        CustomerHelper customerHelper = new CustomerHelper(LoginActivity.this);
                        Customer customer = customerHelper.getCustomerInfo(username);
                        if(customer != null){
                            GlobalSession.getSession().setUserData(
                                    customer.getId(),
                                    customer.getName(),
                                    customer.getEmail(),
                                    customer.getTel(),
                                    customer.getUrl_avatar(),
                                    customer.getGender(),
                                    customer.getBirthday(),
                                    customer.getPassword(),
                                    customer.getAddress()
                            );
                        }
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    } else {
                        err_message.setText("Tên đăng nhập hoặc mật khẩu không chính xác!");
                    }
                }
            }
        });
    }

    // function khởi tạo giá trị
    void init() {
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.link_register);
        edt_mail = findViewById(R.id.input_mail);
        edt_password = findViewById(R.id.input_password);
        err_message = findViewById(R.id.error_message);
        layout_pass = findViewById(R.id.textInputLayoutMail);
        layout_email = findViewById(R.id.textInputLayoutPassword);
    }

    private void validateField(TextInputLayout layout, String value) {
        if (value.isEmpty()) {
            layout.setError("Không được để trống !");
        } else {
            layout.setError(null); // Xóa lỗi
        }
    }
}