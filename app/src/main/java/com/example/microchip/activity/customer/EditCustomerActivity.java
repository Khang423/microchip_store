package com.example.microchip.activity.customer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.microchip.R;
import com.example.microchip.db.CustomerHelper;
import com.example.microchip.model.ProductType;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditCustomerActivity extends AppCompatActivity {
    Button btn_change_image, btn_add;
    CircleImageView imageReview;
    TextInputEditText input_name, input_tel, input_mail, input_password,input_gender,input_birthday,input_address;
    TextInputLayout textInputLayoutName,textInputLayoutTel,textInputLayoutMail,textInputLayoutPassword,textInputLayoutGender,textInputLayoutBirthday,textInputLayoutAddress;

    SQLiteDatabase db = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        init();
        //xử lý điều kiện nút chọn ảnh khi chọn sẽ yêu cầu cấp quyền
        btn_change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(EditCustomerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditCustomerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    changeImage();
                }
            }
        });
        // Xử lý thêm tài khoản vào db
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
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
        input_tel = findViewById(R.id.input_tel);
        input_mail = findViewById(R.id.input_mail);
        input_password = findViewById(R.id.input_password);
        input_address = findViewById(R.id.input_address);
        input_birthday = findViewById(R.id.input_birthday);
        input_gender = findViewById(R.id.input_gender);
        textInputLayoutAddress = findViewById(R.id.textInputLayoutAddress);
        textInputLayoutBirthday = findViewById(R.id.textInputLayoutBirthday);
        textInputLayoutGender = findViewById(R.id.textInputLayoutGender);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutMail = findViewById(R.id.textInputLayoutMail);
        textInputLayoutTel = findViewById(R.id.textInputLayoutTel);
        textInputLayoutName = findViewById(R.id.textInputLayoutName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
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

    private String saveImageToExternalStorage(Uri imageUri) {
        if (imageUri == null) return null;

        try {
            File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages");
            if (!directory.exists() && !directory.mkdirs()) {
                Toast.makeText(this, "Không thể tạo thư mục!", Toast.LENGTH_SHORT).show();
                return null;
            }

            String timeStamp = String.valueOf(System.currentTimeMillis());
            File file = new File(directory, "customer_img_" + timeStamp + ".png");

            // Kiểm tra nếu không mở được InputStream
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream == null) {
                Toast.makeText(this, "Lỗi khi mở ảnh!", Toast.LENGTH_SHORT).show();
                return null;
            }

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

           // Toast.makeText(this, "Hình ảnh đã lưu tại: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu ảnh!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    public void add() {
        String name = input_name.getText().toString().trim();
        String tel = input_tel.getText().toString().trim();
        String email = input_mail.getText().toString().trim();
        String password = input_password.getText().toString().trim();
        String address = input_address.getText().toString().trim();
        String birthday = input_birthday.getText().toString().trim();

        validateField(textInputLayoutName, name);
        validateField(textInputLayoutMail, email);
        validateField(textInputLayoutPassword, password);
        validateField(textInputLayoutTel, tel);


        int gender = 0;
        String genderStr = input_gender.getText().toString().trim();
        if (!genderStr.isEmpty()) {
            try {
                gender = Integer.parseInt(genderStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giới tính không hợp lệ! Vui lòng nhập số.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (name.isEmpty() || tel.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || birthday.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh trước!", Toast.LENGTH_SHORT).show();
            return;
        }

        String imgPath = saveImageToExternalStorage(selectedImageUri);
        if (imgPath == null) {
            Toast.makeText(this, "Lưu ảnh thất bại! Hãy thử lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        CustomerHelper dbHelper = new CustomerHelper(this);
        try {
            dbHelper.addCustomer(name, email, tel, imgPath, gender, birthday, password, address);
            //Toast.makeText(this, "Thêm tài khoản thành công!", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (SQLException e) {
            Toast.makeText(this, "Lỗi khi thêm tài khoản: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private List<ProductType> getListProductType(){
        List<ProductType> list = new ArrayList<>();
        db = openOrCreateDatabase("microchip.db",MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("select * from product_type", null);

        if(cursor.moveToFirst()){
            do {
                ProductType productType = new ProductType(
                        cursor.getInt(0),//id
                        cursor.getString(1)
                );
                list.add(productType);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private void validateField(TextInputLayout layout, String value) {
        if (value.isEmpty()) {
            layout.setError("Không được để trống !");
        } else {
            layout.setError(null); // Xóa lỗi
        }
    }

}