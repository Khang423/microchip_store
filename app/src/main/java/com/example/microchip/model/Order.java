package com.example.microchip.model;

public class Order {
    private int id;
    private int customer_id;
    private double total;
    private int status;
    private String created_at;
    private String Address;

    public Order(int id, int customer_id, double total, int status, String created_at, String address) {
        this.id = id;
        this.customer_id = customer_id;
        this.total = total;
        this.status = status;
        this.created_at = created_at;
        Address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
