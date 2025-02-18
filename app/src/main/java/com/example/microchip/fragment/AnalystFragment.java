package com.example.microchip.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.Calendar;

public class AnalystFragment extends Fragment {
    private View view;
    private BarChart barchart;
    private Button check;
    private TextInputEditText inputFromDate, inputToDate;
    private TextInputLayout layoutToDate, layoutFromDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analyst, container, false);
        init();
        setupListeners();
        return view;
    }

    private void init() {
        barchart = view.findViewById(R.id.barchart);
        inputFromDate = view.findViewById(R.id.input_from_date);
        inputToDate = view.findViewById(R.id.input_to_date);
        check = view.findViewById(R.id.btn_check);
        layoutFromDate = view.findViewById(R.id.layoutFromDate);
        layoutToDate = view.findViewById(R.id.layoutToDate);
    }

    private void setupListeners() {
        inputFromDate.setOnClickListener(v -> showDatePickerDialog(inputFromDate));
        inputToDate.setOnClickListener(v -> showDatePickerDialog(inputToDate));

        check.setOnClickListener(v -> {
            String fromDate = inputFromDate.getText().toString();
            String toDate = inputToDate.getText().toString();

            if (fromDate.isEmpty()) {
                layoutFromDate.setError("Không được để trống");
            } else {
                layoutFromDate.setError(null);
            }
            if (toDate.isEmpty()) {
                layoutToDate.setError("Không được để trống");
            } else {
                layoutToDate.setError(null);
            }

            setupBarChart(fromDate, toDate);
        });
    }

    private void showDatePickerDialog(TextInputEditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            editText.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void setupBarChart(String fromDate, String toDate) {
        OrderHelper orderHelper = new OrderHelper(getContext());
        ArrayList<String> dateLabel = new ArrayList<>();
        ArrayList<BarEntry> entries = orderHelper.getData(fromDate, toDate, dateLabel);
        Log.d("entries", "" + entries);

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo ngày");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barchart.setData(barData);

        XAxis xAxis = barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabel));

        Description description = new Description();
        description.setText("Thống kê doanh thu theo ngày");
        barchart.setDescription(description);
        barchart.animateY(1000);
    }
}
