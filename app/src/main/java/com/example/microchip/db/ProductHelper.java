package com.example.microchip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ProductHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "microchip.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase db;
    private final Context context;

    public ProductHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

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
        values.put("status", 0);

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
        values.put("status", 0);

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
        ContentValues values = new ContentValues();
        values.put("status",1);
        db.update("product", values,"id = ?", new String[]{String.valueOf(product_id)});
        Toast.makeText(context,"Xoá sản phẩm thành công",Toast.LENGTH_LONG).show();
        db.close();
    }
}
