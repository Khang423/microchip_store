package com.example.microchip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AddAccountActivity extends AppCompatActivity {
    Button btn_change_image,btn_add;
    ImageView imageReview;
    TextInputEditText input_name,input_tel,input_mail,input_password;

    SQLiteDatabase db = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        init();
        //xử lý điều kiện nút chọn ảnh khi chọn sẽ yêu cầu cấp quyền
        btn_change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AddAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddAccountActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    changeImage();
                }
            }
        });
        // Xử lý thêm tài khoản vào db
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });
    }

    private void changeImage() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    public void init() {
        btn_change_image = findViewById(R.id.btn_change_image);
        imageReview = findViewById(R.id.imageReview);
        btn_add = findViewById(R.id.btn_add);
        input_name = findViewById(R.id.input_name);
        input_tel  = findViewById(R.id.input_tel);
        input_mail = findViewById(R.id.input_mail);
        input_password = findViewById(R.id.input_password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageReview.setImageURI(selectedImageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeImage();
            } else {
                Toast.makeText(this, "Cấp quyền không thành công ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void insert()
    {
        try {
            db = openOrCreateDatabase("microchip.db", Context.MODE_PRIVATE, null);

            String name = input_name.getText().toString();
            String tel = input_tel.getText().toString();
            String mail = input_mail.getText().toString();
            String password = input_password.getText().toString();

            db.execSQL("INSERT INTO member(name, mail, tel, avatar, address, password) " +
                            "VALUES(?, ?, ?, ?, ?, ?)", new String[]{name, mail, tel, "","", password});

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
}