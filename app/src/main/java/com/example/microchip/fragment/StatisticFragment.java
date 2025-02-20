package com.example.microchip.fragment;

import android.app.DatePickerDialog;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.microchip.R;
import com.example.microchip.db.OrderHelper;
import com.example.microchip.model.OrderData;
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
import java.util.List;
import java.util.Locale;

public class StatisticFragment extends Fragment {
    private View view;
    private BarChart barchart;
    private Button check, btn_search;
    private TextInputEditText inputFromDate, inputToDate, input_search;
    private TextInputLayout layoutToDate, layoutFromDate;
    TableLayout tableLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analyst, container, false);
        init();
        setupListeners();
        inputFromDate.setOnClickListener(v -> showDatePickerDialog(inputFromDate));
        inputToDate.setOnClickListener(v -> showDatePickerDialog(inputToDate));

        btn_search.setOnClickListener(v -> {

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

            String keyword = input_search.getText().toString();
            tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
            loadOrderData(keyword, fromDate, toDate);
        });
        return view;
    }

    private void init() {
        barchart = view.findViewById(R.id.barchart);
        inputFromDate = view.findViewById(R.id.input_from_date);
        inputToDate = view.findViewById(R.id.input_to_date);
        check = view.findViewById(R.id.btn_check);
        layoutFromDate = view.findViewById(R.id.layoutFromDate);
        layoutToDate = view.findViewById(R.id.layoutToDate);
        tableLayout = view.findViewById(R.id.table);
        input_search = view.findViewById(R.id.input_search);
        btn_search = view.findViewById(R.id.btn_search);
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


    private void loadOrderData(String keyword, String fromday, String today) {
        OrderHelper orderHelper = new OrderHelper(getActivity());
        List<OrderData> orderList = orderHelper.getOrderData(keyword, fromday, today);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (OrderData order : orderList) {
            TableRow row = new TableRow(getContext());

            // Format giá tiền sang VND
            String formattedPrice = currencyFormat.format(order.getPrice());
            String formattedTotal = currencyFormat.format(order.getTotal_price());

            // Tạo các TextView để hiển thị dữ liệu
            TextView nameTextView = createTextView(order.getName());
            TextView quantityTextView = createTextView(String.valueOf(order.getQuantity()));
            TextView priceTextView = createTextView(formattedPrice);
            TextView totalPriceTextView = createTextView(formattedTotal);

            // Thêm các TextView vào TableRow
            row.addView(nameTextView);
            row.addView(quantityTextView);
            row.addView(priceTextView);
            row.addView(totalPriceTextView);

            // Thêm TableRow vào TableLayout
            tableLayout.addView(row);
        }
    }


    private TextView createTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(14);
        textView.setPadding(10, 10, 10, 10);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
