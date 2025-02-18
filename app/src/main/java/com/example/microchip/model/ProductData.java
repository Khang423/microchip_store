package com.example.microchip.model;

import java.io.Serializable;

public class ProductData implements Serializable {
    private int id;
    private double price;
    private int quantity;
    private String name;
    private String img_url;

    public ProductData(int id, double price, int quantity, String name, String img_url) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
        this.img_url = img_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
