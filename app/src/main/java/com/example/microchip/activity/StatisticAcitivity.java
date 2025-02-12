package com.example.microchip.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.microchip.R;
import com.example.microchip.db.OrderHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatisticAcitivity extends AppCompatActivity {
    private BarChart barchart;
    Button check;
    TextInputEditText inputFromDate,inputToDate;
    TextInputLayout layoutToDate,layoutFromDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_acitivity);
        init();

        inputFromDate.setOnClickListener(v -> showDatePickerDialogFromDate());
        inputToDate.setOnClickListener(v -> showDatePickerDialogToDate());

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromDate = inputFromDate.getText().toString();
                String toDate = inputToDate.getText().toString();

                if(fromDate.isEmpty()){
                    layoutFromDate.setError("Không được để trống");
                }else{
                    layoutFromDate.setError(null);
                }
                if(toDate.isEmpty()){
                    layoutToDate.setError("Không được để trống");
                }else{
                    layoutToDate.setError(null);
                }

                setupBarChart(fromDate,toDate);
            }
        });

    }
    private void setupBarChart(String fromDate,String toDate) {
        OrderHelper orderHelper = new OrderHelper(StatisticAcitivity.this);
        ArrayList<String> dateLabel =  new ArrayList<>();
        ArrayList<BarEntry> entries = orderHelper.getData(fromDate,toDate,dateLabel);
        Log.d("entries :", ""+entries);

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo ngày");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);


        BarData barData = new BarData(dataSet);
        barchart.setData(barData);

        XAxis xAxis = barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Đảm bảo từng ngày hiển thị một lần
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabel));

        Description description = new Description();
        description.setText("Thống kê doanh thu theo ngày");
        barchart.setDescription(description);
        barchart.animateY(1000);

    }



    private void showDatePickerDialogFromDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            inputFromDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showDatePickerDialogToDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            inputToDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    public void init(){
        barchart = findViewById(R.id.barchart);
        inputFromDate = findViewById(R.id.input_from_date);
        check = findViewById(R.id.btn_check);
        inputToDate = findViewById(R.id.input_to_date);
        layoutFromDate = findViewById(R.id.layoutFromDate);
        layoutToDate = findViewById(R.id.layoutToDate);
    }
}