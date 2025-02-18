package com.example.microchip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.microchip.GlobalSession;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class AuthHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "microchip.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase db;
    private final Context context;

    public AuthHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean checkLoginCustomer(String mail, String password) {
        Cursor cursor = null;
        boolean result = false;

        try {
            db = this.getReadableDatabase();
            String query = "SELECT password FROM customer WHERE email = ?";
            cursor = db.rawQuery(query, new String[]{mail});

            if (cursor != null && cursor.moveToFirst()) {
                String hashedPasswordFromDB = cursor.getString(0);

                // Kiểm tra mật khẩu với BCrypt
                result = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromDB).verified;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return result;
    }


    public void logOut() {
        GlobalSession.getSession().clearSession();
    }
}
