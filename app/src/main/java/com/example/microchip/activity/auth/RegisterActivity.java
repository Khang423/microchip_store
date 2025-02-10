package com.example.microchip.activity.auth;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.microchip.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    TextView btn_login;
    TextInputEditText input_mail, input_name, input_password, input_re_password;
    TextInputLayout textInputLayoutMail, textInputLayoutName, textInputLayoutPassword,textInputLayoutRePassword;
    Button btn_register;

    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        // xử lý chuyển sang trang login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        // xử lý nút đăng ký
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = input_name.getText().toString().trim();
                String email = input_mail.getText().toString().trim();
                String password = input_password.getText().toString().trim();
                String rePassword = input_re_password.getText().toString().trim();

                boolean isValid = true;

                validateField(textInputLayoutName,name);
                validateField(textInputLayoutMail,email);
                validateField(textInputLayoutPassword,password);
                validateField(textInputLayoutRePassword,rePassword);

                if (isValid) {
                    if (checkEmailExists(email)) {
                        Toast.makeText(RegisterActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        addCustomer(name, email, password);
                        Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void init() {
        btn_login = findViewById(R.id.link_login);
        btn_register = findViewById(R.id.btn_register);
        input_mail = findViewById(R.id.input_mail);
        input_name = findViewById(R.id.input_name);
        input_password = findViewById(R.id.input_password);
        input_re_password = findViewById(R.id.input_re_password);
        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutMail = findViewById(R.id.textInputLayoutMail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutRePassword = findViewById(R.id.textInputLayoutRePassword);
    }

    public void addCustomer(String name, String mail, String password) {
        try {
            db = openOrCreateDatabase("microchip.db", Context.MODE_PRIVATE, null);

            db.execSQL("INSERT INTO customer( name, email, tel, avatar, gender, birthday, password, address) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?)", new String[]{name, mail, "", "", "", "", password, ""});
            setResult(RESULT_OK);
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra log để dễ dàng debug
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (db != null) {
                db.close();
                finish();
            }
        }
    }

    public boolean checkEmailExists(String email) {
        Boolean rs = false;
        db = openOrCreateDatabase("microchip.db", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("Select * From customer where email = ?", new String[]{email});
        rs = c.getCount() > 0;
        c.close();
        db.close();
        return rs;
    }
    private void validateField(TextInputLayout layout, String value) {
        if (value.isEmpty()) {
            layout.setError("Không được để trống !");
        } else {
            layout.setError(null); // Xóa lỗi
        }
    }
}