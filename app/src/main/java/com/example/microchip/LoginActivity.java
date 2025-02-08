package com.example.microchip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    TextView btn_register;
    TextInputEditText edt_mail, edt_password;
    TextInputLayout layout_email,layout_pass;
    SQLiteDatabase db = null;

    TextView err_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_mail.getText().toString();
                String password = edt_password.getText().toString();

                if(username.isEmpty()) {
                     layout_email.setError("Không được để trống !");
                }else {
                    layout_email.setError(null); // Xóa lỗi
                }

                if (password.isEmpty()) {
                    layout_pass.setError("Không được để trống !");
                }else {
                    layout_pass.setError(null); // Xóa lỗi
                }

                if( !(username.isEmpty()) && !(password.isEmpty()) ){
                    if(checkLoginCustomer(username,password)) {
                        Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                        startActivity(intent);
                    }else{
                        err_message.setText("Tên đăng nhập hoặc mật khẩu không chính xác!");
                    }
                }
            }
        });
    }
    // function khởi tạo giá trị
    void init()
    {
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.link_register);
        edt_mail = findViewById(R.id.input_mail);
        edt_password = findViewById(R.id.input_password);
        err_message = findViewById(R.id.error_message);
        layout_pass = findViewById(R.id.textInputLayoutMail);
        layout_email = findViewById(R.id.textInputLayoutPassword);
    }
    // function check login cho khach hang
    private boolean checkLoginCustomer(String mail, String password){
        Cursor c = null;
        Boolean rs = false;

        try {
            db = openOrCreateDatabase("microchip.db",MODE_PRIVATE,null);
            String query = "select * from customer where email = ? and password = ?";
            c = db.rawQuery(query,new String[]{mail,password});
            rs = c != null && c.getCount() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if( c != null)
            {
                c.close();
            }
            if( db != null)
            {
                db.close();
            }
        }
        return rs;
    }
}