package com.example.microchip.activity.product;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.microchip.db.DatabaseHelper;
import com.example.microchip.R;
import com.example.microchip.db.ProductHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditProductActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutName, textInputLayoutProductType, textInputLayoutClockSpeed, textInputLayoutFlash,
            textInputLayoutPSram, textInputLayoutWifi, textInputLayoutBT, textInputLayoutGPIO, textInputLayoutADC, textInputLayoutDAC, textInputLayoutProductBrand, textInputLayoutProductPrice;
    TextInputEditText input_name, input_product_type, input_cpu, input_clock_speed, input_flash_ram, input_psram,
            input_wifi, input_bt, input_gpio, input_adc_channel, input_dac_channel, input_brand, input_price;

    ImageView img_product_review;
    Button change_image, btn_add_product;

    SQLiteDatabase db = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        init();
        getValueFromIntent();


        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(EditProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    changeImage();
                }
            }
        });

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    // hàm khởi tạo biến
    public void init() {
        input_price = findViewById(R.id.input_price);
        input_name = findViewById(R.id.input_name);
        input_product_type = findViewById(R.id.input_product_type);
        input_cpu = findViewById(R.id.input_cpu);
        input_clock_speed = findViewById(R.id.input_clock_speed);
        input_flash_ram = findViewById(R.id.input_flash_ram);
        input_psram = findViewById(R.id.input_psram);
        input_wifi = findViewById(R.id.input_wifi);
        input_bt = findViewById(R.id.input_bt);
        input_gpio = findViewById(R.id.input_gpio);
        input_adc_channel = findViewById(R.id.input_adc_channel);
        input_dac_channel = findViewById(R.id.input_dac_channel);
        input_brand = findViewById(R.id.input_brand);
        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutProductType = findViewById(R.id.textInputLayoutProductType);
        textInputLayoutClockSpeed = findViewById(R.id.textInputLayoutClockSpeed);
        textInputLayoutFlash = findViewById(R.id.textInputLayoutFlash);
        textInputLayoutPSram = findViewById(R.id.textInputLayoutPSram);
        textInputLayoutWifi = findViewById(R.id.textInputLayoutWifi);
        textInputLayoutBT = findViewById(R.id.textInputLayoutBT);
        textInputLayoutGPIO = findViewById(R.id.textInputLayoutGPIO);
        textInputLayoutADC = findViewById(R.id.textInputLayoutADC);
        textInputLayoutDAC = findViewById(R.id.textInputLayoutDAC);
        textInputLayoutProductBrand = findViewById(R.id.textInputLayoutProductBrand);
        textInputLayoutProductPrice = findViewById(R.id.textInputLayoutProductPrice);
        img_product_review = findViewById(R.id.img_product_review);
        change_image = findViewById(R.id.change_image);
        btn_add_product = findViewById(R.id.btn_add_product);
    }

    private void changeImage() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            img_product_review.setImageURI(selectedImageUri);
        }
    }

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
            File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Product");
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
        int id = getIntent().getIntExtra("id", -1);
        String oldUriImg = getIntent().getStringExtra("img");

        Uri newImageUri = selectedImageUri;

        String name = input_name.getText().toString();
        String cpu = input_cpu.getText().toString();
        String flash_size = input_flash_ram.getText().toString();
        String psram_size = input_psram.getText().toString();

        // Chuyển đổi từ String sang int và xử lý ngoại lệ
        int clock_speed = parseInt(input_clock_speed.getText().toString());
        int wifi_support = parseInt(input_wifi.getText().toString());
        int bt_support = parseInt(input_bt.getText().toString());
        int gpio_count = parseInt(input_gpio.getText().toString());
        int adc_channels = parseInt(input_adc_channel.getText().toString());
        int dac_channels = parseInt(input_dac_channel.getText().toString());
        int product_type_id = parseInt(input_product_type.getText().toString());
        String brand = input_brand.getText().toString();
        Double price = Double.valueOf(input_price.getText().toString());

        // Kiểm tra nếu chưa chọn ảnh thì không lưu
        String imgPath = (newImageUri != null) ? saveImageToExternalStorage(newImageUri, oldUriImg) : oldUriImg;

        if (imgPath == null) {
            Toast.makeText(this, "Lưu ảnh thất bại!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProductHelper dbHelper = new ProductHelper(this);
        try {
            dbHelper.updateProduct(id, name, imgPath, cpu, clock_speed, flash_size, psram_size, wifi_support, bt_support, gpio_count, adc_channels, dac_channels, product_type_id, brand, price);
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            setResult(EditProductActivity.RESULT_OK, resultIntent);
            finish();
        } catch (SQLException e) {
            Toast.makeText(this, "Lỗi khi cập nhật sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức hỗ trợ để chuyển đổi String sang int
    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0; // Hoặc xử lý theo cách khác nếu cần
        }
    }

    public void getValueFromIntent() {
        int id = getIntent().getIntExtra("id", -1);
        String name = getIntent().getStringExtra("name");
        String cpu = getIntent().getStringExtra("cpu");
        String img = getIntent().getStringExtra("img");
        String flash_size = getIntent().getStringExtra("flash_size");
        int clock_speed = getIntent().getIntExtra("clock_speed", 0);
        String psram_size = getIntent().getStringExtra("psram_size");
        int wifi_sp = getIntent().getIntExtra("wifi_sp", 0);
        int bt_sp = getIntent().getIntExtra("bt_sp", 0);
        int gpio_count = getIntent().getIntExtra("gpio_count", 0);
        int adc_channel = getIntent().getIntExtra("adc_channel", 0);
        int dac_channel = getIntent().getIntExtra("dac_channel", 0);
        int product_type_id = getIntent().getIntExtra("product_type_id", 0);
        String brand = getIntent().getStringExtra("brand");
        double price = getIntent().getDoubleExtra("price", 0.0);
        setValue(name, img, cpu, clock_speed, flash_size, psram_size, wifi_sp, bt_sp, gpio_count, adc_channel, dac_channel, product_type_id, brand, price);
    }


    public void setValue(String name, String url_img, String cpu, int clock_speed, String flash_size,
                         String psram_size, int wifi_sp, int bt_sp, int gpio_count, int adc_channels,
                         int dac_channels, int product_type_id, String brand, Double price) {
        input_name.setText(name);
        img_product_review.setImageURI(Uri.parse(url_img));
        input_cpu.setText(cpu);
        input_clock_speed.setText(String.valueOf(clock_speed));
        input_flash_ram.setText(flash_size);
        input_psram.setText(psram_size);
        input_wifi.setText(String.valueOf(wifi_sp));
        input_bt.setText(String.valueOf(bt_sp));
        input_gpio.setText(String.valueOf(gpio_count));
        input_adc_channel.setText(String.valueOf(adc_channels));
        input_dac_channel.setText(String.valueOf(dac_channels));
        input_price.setText(String.valueOf(price));
        input_product_type.setText(String.valueOf(product_type_id));
        input_brand.setText(brand);
    }

}