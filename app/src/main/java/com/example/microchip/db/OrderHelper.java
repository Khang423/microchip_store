package com.example.microchip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.microchip.activity.StatisticAcitivity;
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

    public void addOrder(int customer_Id,String address,String created_at) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("customer_id", customer_Id);
        values.put("total", 0.0);
        values.put("status", 1);
        values.put("created_at",created_at );
        values.put("address", address);

        long rs = db.insert("[order]", null, values);
        db.close();
//
//        if (rs == -1) {
////            Toast.makeText(context, "Tạo giỏ hàng không thành công", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Tạo giỏ hàng thành công", Toast.LENGTH_SHORT).show();
//        }
    }

    public void deleteOrder(int order_id) {
        db = this.getWritableDatabase();
        db.delete("[order]", "id = ?", new String[]{String.valueOf(order_id)});
        db.delete("[order_detail]", "[order_id] = ?", new String[]{String.valueOf(order_id)});
        db.close();
    }

    public void order(int order_id, double total, String created_at, String address) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("total", total);
        values.put("created_at", created_at);
        values.put("address", address);
        values.put("status", 2);
        if (checkProductExists(order_id)) {
            db.update("[order]", values, "id = ?", new String[]{String.valueOf(order_id)});
            Toast.makeText(context, "Đặt hàng  thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Không có sản phẩm trong giỏ hàng để đặt hàng", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkProductExists(int order_id) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from [order_detail] where order_id = ?", new String[]{String.valueOf(order_id)});

        boolean exists = (cursor != null && cursor.moveToFirst());

        if (cursor != null) {
            cursor.close(); // Đóng Cursor để tránh rò rỉ bộ nhớ
        }// Đóng database sau khi sử dụng
        return exists;
    }

    public ArrayList<BarEntry> getData(String fromData, String toDate, ArrayList<String> dateLabel) {
        db = this.getReadableDatabase();
        ArrayList<BarEntry> entries = new ArrayList<>();
        HashMap<String, Integer> dateIndexMap = new HashMap<>();
        String sql1 = "SELECT created_at, total_price ,name FROM ( " +
                "    SELECT o.created_at, p.name ," +
                "           SUM(od.price * od.quantity) AS total_price, " +
                "           ROW_NUMBER() OVER (PARTITION BY o.created_at ORDER BY SUM(od.price * od.quantity) DESC) AS rn " +
                "    FROM [order] o " +
                "    JOIN order_detail od ON o.id = od.order_id " +
                "    JOIN product p ON od.product_id = p.id " +
                "    WHERE o.created_at BETWEEN ? AND ? " +
                "    GROUP BY o.created_at, od.product_id " +
                ") AS daily_totals " +
                "WHERE rn = 1 " +
                "ORDER BY created_at";

//        String sql1 = "select o.created_at,sum(od.price * od.quantity) as total_price " +
//                "from [order] o join order_detail od on o.id =od.order_id " +
//                "where o.created_at between ? and ? " +
//                "group by o.created_at order by o.created_at asc";

        Cursor cursor = db.rawQuery(sql1, new String[]{fromData, toDate});
        int i = 1;
        Log.d("sql", "" + new String[]{fromData, toDate});
        while (cursor.moveToNext()) {
            String created_at = cursor.getString(0); // Ngày tạo
            float price = cursor.getFloat(1); // Giá tiền
            String product_name = cursor.getString(2);
            dateLabel.add(created_at);

            Log.d("ORDER_STATS", "Ngày: " + product_name + " - Tổng tiền: " + price);

            // Nếu ngày chưa có trong danh sách, thêm vào HashMap
            if (!dateIndexMap.containsKey(created_at)) {
                dateIndexMap.put(created_at, i);
                i++;
            }

            float xValue = dateIndexMap.get(created_at);
            entries.add(new BarEntry(xValue, price));
        }
        Log.d("date", "" + dateLabel);
        cursor.close();
        db.close();
        return entries;
    }

    public ArrayList<String> getDatesList() {
        ArrayList<String> dates = new ArrayList<>();
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  DISTINCT created_at FROM [order] ORDER BY created_at ASC", null);

        while (cursor.moveToNext()) {
            dates.add(cursor.getString(0)); // Lấy danh sách ngày
        }

        cursor.close();
        db.close();
        Log.d("date :", "" + dates);
        return dates;
    }
}
