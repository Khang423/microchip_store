package com.example.microchip;

import java.util.Date;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String tel;
    private String url_avatar;
    private int gender;
    private String birthday;
    private String password;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUrl_avatar() {
        return url_avatar;
    }

    public void setUrl_avatar(String url_avatar) {
        this.url_avatar = url_avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Customer(int id, String name, String email, String tel, String url_avatar, int gender, String birthday, String password, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.url_avatar = url_avatar;
        this.gender = gender;
        this.birthday = birthday;
        this.password = password;
        this.address = address;
    }

}
