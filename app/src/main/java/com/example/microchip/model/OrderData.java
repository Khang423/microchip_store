package com.example.microchip.model;

public class OrderData {
    String name;
    int quantity;
    double price;
    double total_price;

    public OrderData(String name, int quantity, double price, double total_price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
