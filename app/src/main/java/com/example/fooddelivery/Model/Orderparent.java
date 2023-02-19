package com.example.fooddelivery.Model;

import java.util.Date;

import kotlin.jvm.Transient;

public class Orderparent {
    private String id;
    private String customer_name;
    private Date order_time;
    private Date require_time;
    private String address_ship;
    private String shipper_name;
    private String status;
    private Float sum_price;
    private Long amountOrder;
    private String phonenumber;


    @Transient
    private  String foodName;
    @Transient
    private String imgUrl;

    public Orderparent() {
    }

    public Orderparent(String id, String customer_name, Date order_time, Date require_time, String address_ship, String shipper_name, String status, Float sum_price, Long amountOrder,String phonenumber) {
        this.id = id;
        this.customer_name = customer_name;
        this.order_time = order_time;
        this.require_time = require_time;
        this.address_ship = address_ship;
        this.shipper_name = shipper_name;
        this.status = status;
        this.sum_price = sum_price;
        this.amountOrder = amountOrder;
        this.phonenumber = phonenumber;
    }

    public Long getAmountOrder() {
        return amountOrder;
    }

    public void setAmountOrder(Long amountOrder) {
        this.amountOrder = amountOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public Date getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Date order_time) {
        this.order_time = order_time;
    }

    public Date getRequire_time() {
        return require_time;
    }

    public void setRequire_time(Date require_time) {
        this.require_time = require_time;
    }

    public String getAddress_ship() {
        return address_ship;
    }

    public void setAddress_ship(String address_ship) {
        this.address_ship = address_ship;
    }

    public String getShipper_name() {
        return shipper_name;
    }

    public void setShipper_name(String shipper_name) {
        this.shipper_name = shipper_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getSum_price() {
        return sum_price;
    }

    public void setSum_price(Float sum_price) {
        this.sum_price = sum_price;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
