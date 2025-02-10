package com.example.microchip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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
}
