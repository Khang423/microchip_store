package com.example.microchip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "microchip.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase db;
    private final Context context;

    public OrderHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addOrder(int customer_Id, double total, int status,String created_at, String address){
        db =this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("customer_id",customer_Id);
        values.put("total",total);
        values.put("status",status);
        values.put("created_at",created_at);
        values.put("address",address);

        long rs = db.insert("[order]",null,values);
        db.close();

        if(rs == -1){
            Toast.makeText(context,"Tạo giỏ hàng không thành công",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Tạo giỏ hàng thành công",Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOrder(int order_id) {
        db = this.getWritableDatabase();
        db.delete("[order]", "id = ?", new String[]{String.valueOf(order_id)});
        db.delete("[order_detail]", "[order_id] = ?", new String[]{String.valueOf(order_id)});
        db.close();
    }

    public void order(int order_id,double total, String created_at,String address) {
        db = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("total",total);
        values.put("created_at",created_at);
        values.put("address",address);
        values.put("status",2);
        if(checkProductExists(order_id))
        {
            db.update("[order]",values,"id = ?",new String[]{String.valueOf(order_id)});
            Toast.makeText(context, "Đặt hàng  thành công", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Không có sản phẩm trong giỏ hàng để đặt hàng", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkProductExists(int order_id){
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from [order_detail] where order_id = ?",new String[]{String.valueOf(order_id)});

        boolean exists = (cursor != null && cursor.moveToFirst());

        if (cursor != null) {
            cursor.close(); // Đóng Cursor để tránh rò rỉ bộ nhớ
        }// Đóng database sau khi sử dụng
        return exists;
    }

    public ArrayList<BarEntry> getData(String fromData, String toDate) {
        db = this.getReadableDatabase();
        ArrayList<BarEntry> entries = new ArrayList<>();
        HashMap<String, Integer> dateIndexMap = new HashMap<>();

        Cursor cursor = db.rawQuery("SELECT created_at, MAX(total) FROM [order] where created_at BETWEEN ? and ? GROUP BY created_at ORDER BY created_at ASC",new String[]{fromData,toDate});
        int index = 1; // Bắt đầu từ 1

        while (cursor.moveToNext()) {
            String created_at = cursor.getString(0); // Ngày tạo
            float price = cursor.getFloat(1); // Giá tiền

            // Nếu ngày chưa có trong danh sách, thêm vào HashMap
            if (!dateIndexMap.containsKey(created_at)) {
                dateIndexMap.put(created_at, index);
                index++;
            }

            // Lấy số thứ tự của ngày để làm trục X
            int xValue = dateIndexMap.get(created_at);
            entries.add(new BarEntry(xValue, price));
        }

        cursor.close();
        db.close();
        return entries;
    }

}
