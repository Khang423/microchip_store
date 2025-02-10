package com.example.microchip.activity.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.microchip.db.DatabaseHelper;
import com.example.microchip.R;
import com.example.microchip.db.ProductHelper;
import com.example.microchip.model.ProductType;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutName, textInputLayoutProductType, textInputLayoutClockSpeed, textInputLayoutFlash,
            textInputLayoutPSram, textInputLayoutWifi, textInputLayoutBT, textInputLayoutGPIO, textInputLayoutADC, textInputLayoutDAC, textInputLayoutProductBrand, textInputLayoutProductPrice;
    TextInputEditText input_name, input_product_type, input_cpu, input_clock_speed, input_flash_ram, input_psram,
            input_wifi, input_bt, input_gpio, input_adc_channel, input_dac_channel, input_brand, input_price;

    AutoCompleteTextView filled_exposed_dropdown;
    Spinner spinner;
    ImageView img_product_review;
    Button change_image, btn_add_product;

    SQLiteDatabase db = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private Uri selectedImageUri;
    List<ProductType> productTypeList;
    HashMap<String, Integer> productTypeMap = new HashMap<>();

    private Integer selectedProductTypeId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        init();
        productTypeList = getListProduct();

        List<String> strProductTypeList = new ArrayList<>();
        for(ProductType product : productTypeList) {
            strProductTypeList.add(product.getName());
            productTypeMap.put(product.getName(),product.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strProductTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filled_exposed_dropdown.setAdapter(adapter);

        filled_exposed_dropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = adapter.getItem(position);
            if (selectedItem != null && productTypeMap.containsKey(selectedItem)) {
                selectedProductTypeId = productTypeMap.get(selectedItem); // Lưu ID của loại sản phẩm
                Toast.makeText(getApplicationContext(), "Bạn chọn: " + selectedItem + " - ID: " + selectedProductTypeId, Toast.LENGTH_SHORT).show();
            }
        });

        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    changeImage();
                }
            }
        });

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
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
        spinner = findViewById(R.id.spinner);
        filled_exposed_dropdown = findViewById(R.id.filled_exposed_dropdown);

    }
    // function chọn ảnh
    private void changeImage() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }
    // hàm nhận kết cấp quyền truy cập bộ nhớ
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            img_product_review.setImageURI(selectedImageUri);
        }
    }
    // cấp quyền truy cập bộ nhớ
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
    // hàm lưu ảnh
    private String saveImageToExternalStorage(Uri imageUri) {
        if (imageUri == null) return null;

        try {
            File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Product");
            if (!directory.exists() && !directory.mkdirs()) {
                Toast.makeText(this, "Không thể tạo thư mục!", Toast.LENGTH_SHORT).show();
                return null;
            }

            String timeStamp = String.valueOf(System.currentTimeMillis());
            File file = new File(directory, "product_img_" + timeStamp + ".png");

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

            //Toast.makeText(this, "Hình ảnh đã lưu tại: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu ảnh!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void add() {
        String name = input_name.getText().toString().trim();
        String cpu = input_cpu.getText().toString().trim();
        String flash_size = input_flash_ram.getText().toString().trim();
        String psram_size = input_psram.getText().toString().trim();
        String brand = input_brand.getText().toString().trim();
        String priceText = input_price.getText().toString().trim();


        // Kiểm tra nếu chưa chọn ảnh thì không lưu
        if (selectedImageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh trước!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đổi từ String sang số, xử lý lỗi nhập sai
        int clock_speed = parseIntSafe(input_clock_speed.getText().toString());
        int wifi_support = parseIntSafe(input_wifi.getText().toString());
        int bt_support = parseIntSafe(input_bt.getText().toString());
        int gpio_count = parseIntSafe(input_gpio.getText().toString());
        int adc_channels = parseIntSafe(input_adc_channel.getText().toString());
        int dac_channels = parseIntSafe(input_dac_channel.getText().toString());

        if (selectedProductTypeId == null) {
            Toast.makeText(this, "Vui lòng chọn loại sản phẩm hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        int product_type_id = selectedProductTypeId;

        double price = 0.0;
        if (!priceText.isEmpty()) {
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá sản phẩm không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Lưu ảnh vào bộ nhớ
        String imgPath = saveImageToExternalStorage(selectedImageUri);
        if (imgPath == null) {
            Toast.makeText(this, "Lưu ảnh thất bại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm dữ liệu vào database
        ProductHelper dbHelper = new ProductHelper(this);
        try {
            dbHelper.addProduct(name, imgPath, cpu, clock_speed, flash_size, psram_size,
                    wifi_support, bt_support, gpio_count, adc_channels,
                    dac_channels, product_type_id, brand, price);
            Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            setResult(EditProductActivity.RESULT_OK, resultIntent);
            finish();
        } catch (SQLException e) {
            Toast.makeText(this, "Lỗi khi thêm sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private List<ProductType> getListProduct(){
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
}