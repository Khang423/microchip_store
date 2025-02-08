package com.example.microchip;

public class Product {
    public Product(int id, String name, String url_img, String cpu, int clock_speed, String flash_size, String pram_size, int wifi_sp, int bt_sp, int gpio_channels, int adc_channels, int dac_chanels, int product_type_id, String brand) {
        this.id = id;
        this.name = name;
        this.url_img = url_img;
        this.cpu = cpu;
        this.clock_speed = clock_speed;
        this.flash_size = flash_size;
        this.pram_size = pram_size;
        this.wifi_sp = wifi_sp;
        this.bt_sp = bt_sp;
        this.gpio_channels = gpio_channels;
        this.adc_channels = adc_channels;
        this.dac_chanels = dac_chanels;
        this.product_type_id = product_type_id;
        this.brand = brand;
    }

    private int id;

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

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public int getClock_speed() {
        return clock_speed;
    }

    public void setClock_speed(int clock_speed) {
        this.clock_speed = clock_speed;
    }

    public String getFlash_size() {
        return flash_size;
    }

    public void setFlash_size(String flash_size) {
        this.flash_size = flash_size;
    }

    public String getPram_size() {
        return pram_size;
    }

    public void setPram_size(String pram_size) {
        this.pram_size = pram_size;
    }

    public int getWifi_sp() {
        return wifi_sp;
    }

    public void setWifi_sp(int wifi_sp) {
        this.wifi_sp = wifi_sp;
    }

    public int getBt_sp() {
        return bt_sp;
    }

    public void setBt_sp(int bt_sp) {
        this.bt_sp = bt_sp;
    }

    public int getGpio_channels() {
        return gpio_channels;
    }

    public void setGpio_channels(int gpio_channels) {
        this.gpio_channels = gpio_channels;
    }

    public int getAdc_channels() {
        return adc_channels;
    }

    public void setAdc_channels(int adc_channels) {
        this.adc_channels = adc_channels;
    }

    public int getDac_chanels() {
        return dac_chanels;
    }

    public void setDac_chanels(int dac_chanels) {
        this.dac_chanels = dac_chanels;
    }

    public int getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(int product_type_id) {
        this.product_type_id = product_type_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    private String name;
    private String url_img;
    private String cpu;
    private int clock_speed;
    private String flash_size;
    private String pram_size;
    private int wifi_sp;
    private int bt_sp;
    private int gpio_channels;
    private int adc_channels;
    private int dac_chanels;
    private int product_type_id;
    private String brand;
}
