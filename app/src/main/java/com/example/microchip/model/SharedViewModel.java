package com.example.microchip.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> urlImg = new MutableLiveData<>();
    private final MutableLiveData<Integer> productId = new MutableLiveData<>();
    private final MutableLiveData<Double> price = new MutableLiveData<>();
    private final MutableLiveData<List<ProductData>> orderList = new MutableLiveData<>(new ArrayList<>());

    public void setProductData(String url, int id, double productPrice) {
        urlImg.setValue(url);
        productId.setValue(id);
        price.setValue(productPrice);
    }
    public void clearCart() {
        orderList.setValue(new ArrayList<>());
    }

    public LiveData<List<ProductData>> getOrderList() {
        return orderList;
    }
    public void addProductToOrder(ProductData productData) {
        List<ProductData> currentList = orderList.getValue();
        if (currentList != null) {
            currentList.add(productData);
            orderList.setValue(new ArrayList<>(currentList));  // Cập nhật danh sách mới
        }
    }

    // Nhận dữ liệu ảnh
    public LiveData<String> getUrlImg() {
        return urlImg;
    }

    // Nhận dữ liệu ID sản phẩm
    public LiveData<Integer> getProductId() {
        return productId;
    }

    // Nhận dữ liệu giá sản phẩm
    public LiveData<Double> getPrice() {
        return price;
    }
}
