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
        String sql = "INSERT INTO customer (name, email, tel, avatar, gender, birthday, password, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            db.execSQL(sql, new Object[] { name, email, tel, url_avatar, gender, birthday, password, address });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(); // Đảm bảo đóng cơ sở dữ liệu
        }
    }

    public void deleteCustomer(int customer_id) {
        db = this.getWritableDatabase();
        db.delete("customer", "id = ?", new String[]{String.valueOf(customer_id)});
        db.close();
    }

    // PRODUCT


}
