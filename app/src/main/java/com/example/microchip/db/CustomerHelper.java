package com.example.microchip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomerHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "microchip.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase db;
    private final Context context;

    public CustomerHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

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
}
