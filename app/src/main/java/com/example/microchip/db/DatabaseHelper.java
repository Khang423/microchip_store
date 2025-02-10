package com.example.microchip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "microchip.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH = "/data/data/com.example.microchip/databases/";

    SQLiteDatabase db;
    private final Context context;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    private boolean checkDatabaseExists() {
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        return dbFile.exists();
    }
    private void copyDatabaseFromAssets() throws IOException {
        InputStream input = context.getAssets().open("databases/" + DATABASE_NAME);
        String outputFileName = DB_PATH + DATABASE_NAME;
        File dbDir = new File(DB_PATH);

        // Nếu thư mục databases chưa tồn tại, tạo nó
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }

        OutputStream output = new FileOutputStream(outputFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        input.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // CUSTOMER


    // PRODUCT


    // Product Type

}
