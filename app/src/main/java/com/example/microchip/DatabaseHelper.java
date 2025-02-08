package com.example.microchip;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "microchip.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // CUSTOMER
    public void addCustomer(String name, String email, String tel, String url_avatar, int gender, String birthday, String password, String address) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Gán dữ liệu vào ContentValues
        values.put("name", name);
        values.put("email", email);
        values.put("tel", tel);
        values.put("avatar", url_avatar);
        values.put("gender", gender);
        values.put("birthday", birthday);
        values.put("password", password);
        values.put("address", address);

        // Thêm dữ liệu vào bảng customer
        long result = db.insert("customer", null, values);
        db.close(); // Đóng database sau khi dùng

        // Kiểm tra xem có lỗi xảy ra không
        if (result == -1) {
            System.out.println("Thêm khách hàng thất bại!");
        } else {
            System.out.println("Thêm khách hàng thành công!");
        }
    }


    public void deleteCustomer(int customer_id) {
        db = this.getWritableDatabase();
        db.delete("customer", "id = ?", new String[]{String.valueOf(customer_id)});
        db.close();
    }

    // PRODUCT
    public void addProduct(String name, String url_img, String cpu, int clock_speed, String flash_size,
                           String psram_size, int wifi_sp, int bt_sp, int gpio_count, int adc_channels,
                           int dac_channels, int product_type_id, String brand, Double price) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("img", url_img);
        values.put("cpu", cpu);
        values.put("clock_speed", clock_speed);
        values.put("flash_size", flash_size);
        values.put("psram_size", psram_size);
        values.put("wifi_support", wifi_sp);
        values.put("bt_support", bt_sp);
        values.put("gpio_count", gpio_count);
        values.put("adc_channels", adc_channels);
        values.put("dac_channels", dac_channels);
        values.put("product_type_id", product_type_id);
        values.put("brand", brand);
        values.put("price", price);

        long result = db.insert("product", null, values);
        db.close();

        if (result == -1) {
            System.out.println("Thêm sản phẩm thất bại!");
        } else {
            System.out.println("Thêm sản phẩm thành công!");
        }
    }


    public void updateProduct(int id, String name, String url_img, String cpu, int clock_speed, String flash_size,
                              String psram_size, int wifi_sp, int bt_sp, int gpio_count, int adc_channels,
                              int dac_channels, int product_type_id, String brand, Double price) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Gán dữ liệu vào ContentValues
        values.put("name", name);
        values.put("img", url_img);
        values.put("cpu", cpu);
        values.put("clock_speed", clock_speed);
        values.put("flash_size", flash_size);
        values.put("psram_size", psram_size);
        values.put("wifi_support", wifi_sp);
        values.put("bt_support", bt_sp);
        values.put("gpio_count", gpio_count);
        values.put("adc_channels", adc_channels);
        values.put("dac_channels", dac_channels);
        values.put("product_type_id", product_type_id);
        values.put("brand", brand);
        values.put("price", price);

        int rowsAffected = db.update("product", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        if (rowsAffected > 0) {
            System.out.println("Cập nhật sản phẩm thành công!");
        } else {
            System.out.println("Cập nhật sản phẩm thất bại! Không tìm thấy ID.");
        }
    }

    public void deleteProduct(int product_id) {
        db = this.getWritableDatabase();
        db.delete("product", "id = ?", new String[]{String.valueOf(product_id)});
        db.close();
    }
}
