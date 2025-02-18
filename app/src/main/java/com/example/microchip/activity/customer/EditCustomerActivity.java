package com.example.microchip.activity.customer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
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

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.activity.product.EditProductActivity;
import com.example.microchip.db.CustomerHelper;
import com.example.microchip.db.ProductHelper;
import com.example.microchip.model.ProductType;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditCustomerActivity extends AppCompatActivity {
    Button btn_change_image, btn_edit;
    CircleImageView imageReview;
    TextInputEditText input_name, input_tel, input_mail, input_password, input_gender, input_birthday, input_address;
    TextInputLayout textInputLayoutName, textInputLayoutTel, textInputLayoutMail, textInputLayoutPassword, textInputLayoutGender, textInputLayoutBirthday, textInputLayoutAddress;

    SQLiteDatabase db = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        init();
        setDataInput();
        input_birthday.setOnClickListener(v -> showDatePickerDialog());
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
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
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
        btn_edit = findViewById(R.id.btn_edit);
        input_name = findViewById(R.id.input_name);
        input_tel = findViewById(R.id.input_tel);
        input_mail = findViewById(R.id.input_mail);
        input_address = findViewById(R.id.input_address);
        input_birthday = findViewById(R.id.input_birthday);
        input_gender = findViewById(R.id.input_gender);
        textInputLayoutAddress = findViewById(R.id.textInputLayoutAddress);
        textInputLayoutBirthday = findViewById(R.id.textInputLayoutBirthday);
        textInputLayoutGender = findViewById(R.id.textInputLayoutGender);
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

    private String saveImageToExternalStorage(Uri imageUri, String oldImagePath) {
        if (imageUri == null) return oldImagePath; // Trả về ảnh cũ nếu không có ảnh mới

        try {
            // Nếu ảnh mới giống ảnh cũ, không cần lưu lại, trả về ảnh cũ luôn
            if (oldImagePath != null && imageUri.toString().equals(oldImagePath)) {
                return oldImagePath;
            }

            // Tạo thư mục lưu ảnh
            File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Customer");
            if (!directory.exists() && !directory.mkdirs()) {
                Toast.makeText(this, "Không thể tạo thư mục!", Toast.LENGTH_SHORT).show();
                return oldImagePath;
            }

            // Đặt tên file theo timestamp để tránh trùng lặp
            String timeStamp = String.valueOf(System.currentTimeMillis());
            File file = new File(directory, "product_img_" + timeStamp + ".png");

            // Kiểm tra nếu không mở được InputStream
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream == null) {
                Toast.makeText(this, "Lỗi khi mở ảnh!", Toast.LENGTH_SHORT).show();
                return oldImagePath;
            }

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            //Toast.makeText(this, "Hình ảnh đã lưu tại: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu ảnh!", Toast.LENGTH_SHORT).show();
            return oldImagePath;
        }
    }


    public void update() {
        int id = GlobalSession.getSession().getId();
        String oldUriImg = GlobalSession.getSession().getUrl_avatar();

        Uri newImageUri = selectedImageUri;

        String name = input_name.getText().toString().trim();
        String tel = input_tel.getText().toString().trim();
        String email = input_mail.getText().toString().trim();
        String address = input_address.getText().toString().trim();
        String birthday = input_birthday.getText().toString().trim();

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

        String imgPath = (newImageUri != null) ? saveImageToExternalStorage(newImageUri, oldUriImg) : oldUriImg;

        if (imgPath == null) {
            Toast.makeText(this, "Lưu ảnh thất bại!", Toast.LENGTH_SHORT).show();
            return;
        }

        CustomerHelper dbHelper = new CustomerHelper(this);
        try {
            dbHelper.update(id, name, email, tel, imgPath, gender, birthday, address);
            GlobalSession.getSession().clearSession();
            GlobalSession.getSession().setUserData(id, name, email, tel, imgPath, gender, birthday, null, address);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } catch (SQLException e) {
            Toast.makeText(this, "Lỗi khi cập nhật sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void setDataInput() {
        int id = GlobalSession.getSession().getId();
        String name = GlobalSession.getSession().getName();
        String email = GlobalSession.getSession().getEmail();
        String tel = GlobalSession.getSession().getTel();
        String avatar = GlobalSession.getSession().getUrl_avatar();
        int gender = GlobalSession.getSession().getGender();
        String birthday = GlobalSession.getSession().getBirthday();
        String address = GlobalSession.getSession().getAddress();

        imageReview.setImageURI(Uri.parse(avatar));
        input_name.setText(name);
        input_tel.setText(tel);
        input_mail.setText(email);
        input_address.setText(address);
        input_birthday.setText(birthday);
        input_gender.setText(String.valueOf(gender));
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            input_birthday.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    public boolean checkEmailExists(String email, int id) {
        Boolean rs = false;
        db = openOrCreateDatabase("microchip.db", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("Select * From customer where email = ? and id != ?", new String[]{email, String.valueOf(id)});
        rs = c.getCount() > 0;
        c.close();
        db.close();
        return rs;
    }
}