package com.example.microchip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.microchip.model.Product;

public class OrderDetailHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "microchip.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase db;
    private final Context context;

    public OrderDetailHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void add(int order_id, int product_id, double price, int quantity) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantity FROM [order_detail] WHERE order_id = ? AND product_id = ?",
                new String[]{String.valueOf(order_id), String.valueOf(product_id)});

        if (cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put("quantity", currentQuantity + 1);
            db.update("[order_detail]", values, "order_id = ? AND product_id = ?",
                    new String[]{String.valueOf(order_id), String.valueOf(product_id)});
        } else {
            ContentValues values = new ContentValues();
            values.put("order_id", order_id);
            values.put("product_id", product_id);
            values.put("price", price);
            values.put("quantity", quantity);
            db.insert("[order_detail]", null, values);
        }
        cursor.close();
        db.close();

        Toast.makeText(context, "Thêm sản phẩm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
    }


    public Product getInfo(int product_id) {
        db = this.getReadableDatabase();
        Product product = null;
        Cursor cursor = db.rawQuery("select *from product where id = ?", new String[]{String.valueOf(product_id)});

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String url_img = cursor.getString(cursor.getColumnIndexOrThrow("img"));
            String cpu = cursor.getString(cursor.getColumnIndexOrThrow("cpu"));
            int clock_speed = cursor.getInt(cursor.getColumnIndexOrThrow("clock_speed"));
            String flash_size = cursor.getString(cursor.getColumnIndexOrThrow("flash_size"));
            String pram_size = cursor.getString(cursor.getColumnIndexOrThrow("psram_size"));
            int wifi_sp = cursor.getInt(cursor.getColumnIndexOrThrow("wifi_support"));
            int bt_sp = cursor.getInt(cursor.getColumnIndexOrThrow("bt_support"));
            int gpio_channels = cursor.getInt(cursor.getColumnIndexOrThrow("gpio_count"));
            int adc_channels = cursor.getInt(cursor.getColumnIndexOrThrow("adc_channels"));
            int dac_channels = cursor.getInt(cursor.getColumnIndexOrThrow("dac_channels"));
            int product_type_id = cursor.getInt(cursor.getColumnIndexOrThrow("product_type_id"));
            String brand = cursor.getString(cursor.getColumnIndexOrThrow("brand"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

            product = new Product(id, name, url_img, cpu, clock_speed, flash_size, pram_size, wifi_sp, bt_sp,
                    gpio_channels, adc_channels, dac_channels, product_type_id, brand, price);
        }
        if (cursor != null) cursor.close();
        db.close();

        return product;
    }

    public double totalPrice(int id){
        db = this.getWritableDatabase();
        double rs = 0.0;

        Cursor cursor = db.rawQuery("SELECT SUM(price * quantity) FROM [order_detail] WHERE [order_id] = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            rs = cursor.getDouble(0);
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return rs;
    }

    public void quantityUp(int order_id, int product_id) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantity FROM [order_detail] WHERE order_id = ? AND product_id = ?",
                new String[]{String.valueOf(order_id), String.valueOf(product_id)});

        if (cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put("quantity", currentQuantity + 1);
            db.update("[order_detail]", values, "order_id = ? AND product_id = ?",
                    new String[]{String.valueOf(order_id), String.valueOf(product_id)});
        }
        cursor.close();
        db.close();
    }

    public void quantityDown(int order_id, int product_id) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantity FROM [order_detail] WHERE order_id = ? AND product_id = ?",
                new String[]{String.valueOf(order_id), String.valueOf(product_id)});

        if (cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(0);
            if (currentQuantity > 1) {
                ContentValues values = new ContentValues();
                values.put("quantity", currentQuantity - 1);
                db.update("[order_detail]", values, "order_id = ? AND product_id = ?",
                        new String[]{String.valueOf(order_id), String.valueOf(product_id)});
            } else {
                db.delete("[order_detail]", "order_id = ? AND product_id = ?",
                        new String[]{String.valueOf(order_id), String.valueOf(product_id)});
            }
        }
        cursor.close();
        db.close();
    }

}
