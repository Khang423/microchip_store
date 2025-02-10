package com.example.microchip.activity.productType;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.microchip.R;
import com.example.microchip.activity.product.EditProductActivity;
import com.example.microchip.db.ProductTypeHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddProductTypeActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutName;
    TextInputEditText input_name;
    Button btn_add_product_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_type);
        init();

        btn_add_product_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    public void init(){
        input_name = findViewById(R.id.input_name);
        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        btn_add_product_type = findViewById(R.id.btn_add_product_type);
    }
    public void add() {
        String name = input_name.getText().toString().trim();
        // Thêm dữ liệu vào database
        ProductTypeHelper productTypeHelper =  new ProductTypeHelper(this);
        try {
            productTypeHelper.addProductType(name);
            Intent resultIntent = new Intent();
            setResult(EditProductActivity.RESULT_OK, resultIntent);
            finish();
        } catch (SQLException e) {
            Toast.makeText(this, "Lỗi khi thêm sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}