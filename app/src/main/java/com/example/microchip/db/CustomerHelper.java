package com.example.microchip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.microchip.model.Customer;

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


        if (result == -1) {
            Toast.makeText(context, "Thêm khách hàng thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Thêm khách hàng thành công!", Toast.LENGTH_SHORT).show();
        }
    }

    public void update(int id, String name, String email, String tel, String url_avatar, int gender, String birthday, String password, String address) {
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
        long result = db.update("customer",values, "id = ?",new String[]{String.valueOf(id)});
        db.close(); // Đóng database sau khi dùng


        if (result == -1) {
            Toast.makeText(context, "Cập nhật khách hàng thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Cập nhật hàng thành công!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCustomer(int customer_id) {
        db = this.getWritableDatabase();
        db.delete("customer", "id = ?", new String[]{String.valueOf(customer_id)});
        db.delete("[order]", "customer_id = ?", new String[]{String.valueOf(customer_id)});
        db.close();
//        AuthHelper authHelper = new AuthHelper(context);
//        authHelper.logOut();
    }

    public Customer getCustomerInfo(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM customer WHERE email=?", new String[]{email});

        if (cursor.moveToFirst()) {
            Customer customer = new Customer(
                    cursor.getInt(0),//id
                    cursor.getString(1),//name
                    cursor.getString(2),//email
                    cursor.getString(3),//tel
                    cursor.getString(4),//url avatar
                    cursor.getInt(5),//gender
                    cursor.getString(6),//birthday
                    cursor.getString(7),//password
                    cursor.getString(8)//address
            );
            cursor.close();
            db.close();
            return customer;
        }
        cursor.close();
        db.close();
        return null;
    }



}
