package com.example.microchip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {
    String DB_PATH = "/databases/";
    SQLiteDatabase database = null;
    String DATABASE_NAME = "microchip.db";

    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;

    public static final int RESULT_PRODUCT_ACTIVITY = 1;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();
        showDataToListView();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, AddAccountActivity.class);
                startActivityForResult(intent,RESULT_PRODUCT_ACTIVITY);
            }
        });
    }

    private void processCopy() {
        File dbFile = new File(getDatabaseBPath(DATABASE_NAME));
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying success ", Toast.LENGTH_SHORT).show();
                ;
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void init() {
        btn_add = findViewById(R.id.btn_add);
        lv = findViewById(R.id.lv);
    }

    private void showDataToListView() {
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);
        processCopy();
        database = openOrCreateDatabase("microchip.db", MODE_PRIVATE, null);
        Cursor c = database.query("member", new String[]{"id,name"}, null, null, null, null, null, null);
        String data = " ";
        c.moveToFirst();

        while (c.isAfterLast() == false) {
            data = c.getString(1);
            mylist.add(data);
            c.moveToNext();
        }
        c.close();
        myadapter.notifyDataSetChanged();
    }

    private String getDatabaseBPath(String DATABASE_NAME) {
        return getApplicationInfo().dataDir + DB_PATH + DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset() {
        // TODO Auto-generated method stub
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabaseBPath(DATABASE_NAME);
            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH);
            if (!f.exists())
                f.mkdir();
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            // Truyền bytes dữ liệu từ input đến output
            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        super.onActivityResult(requestCode, resultCode, Data);
        if(requestCode == RESULT_PRODUCT_ACTIVITY){
            if (resultCode == RESULT_OK ) {
                showDataToListView();
                Toast.makeText(this, "Thêm tài khoản thành công!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}