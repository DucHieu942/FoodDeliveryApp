package com.example.fooddelivery.Model;

public class Order {
    private Long id;
    private String orderparent_id;
    private Long food_id;
    private Float price;

    public Order() {
    }

    public Order(Long id, String orderparent_id, Long food_id, Float price) {
        this.id = id;
        this.orderparent_id = orderparent_id;
        this.food_id = food_id;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderparent_id() {
        return orderparent_id;
    }

    public void setOrderparent_id(String orderparent_id) {
        this.orderparent_id = orderparent_id;
    }

    public Long getFood_id() {
        return food_id;
    }

    public void setFood_id(Long food_id) {
        this.food_id = food_id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
