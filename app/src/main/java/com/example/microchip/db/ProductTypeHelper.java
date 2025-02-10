package com.example.microchip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ProductTypeHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "microchip.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase db;
    private final Context context;

    public ProductTypeHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addProductType(String name) {
        db = this.getWritableDatabase();

        if (isProductTypeExists(name)) {
            Toast.makeText(context, "Loại sản phẩm đã tồn tại trong database!", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        long result = db.insert("product_type", null, values);
        db.close();

        if (result == -1) {
            Toast.makeText(context, "Thêm loại sản phẩm thất bại !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Thêm loại sản phẩm thành công !", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isProductTypeExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase(); // Mở database

        String query = "SELECT COUNT(*) FROM product_type WHERE name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name});

        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0; // Nếu COUNT > 0 thì tồn tại
        }

        cursor.close(); // Đóng con trỏ
        return exists;
    }

    public void updateProductType(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (isUpdateProductTypeExists(name, id)) {
            Toast.makeText(context, "Tên loại sản phẩm đã tồn tại, không thể cập nhật!", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("name", name);

        long result = db.update("product_type", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        if (result == -1) {
            Toast.makeText(context, "Cập nhật loại sản phẩm thất bại !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Cập nhật loại sản phẩm thành công !", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isUpdateProductTypeExists(String name, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM product_type WHERE name = ? AND id = ?", new String[]{name, String.valueOf(id)});

        boolean exists = false;
        exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public void deleteProductType(int product_id) {
        db = this.getWritableDatabase();
        db.delete("product_type", "id = ?", new String[]{String.valueOf(product_id)});
        db.close();
    }
}
