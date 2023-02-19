package com.example.fooddelivery.Model;

public class Order {
    private String id;
    private String orderparent_id;
    private Long food_id;
    private Float price;
    private Integer count;

    public Order() {
    }

    public Order(String id, String orderparent_id, Long food_id, Float price,Integer count) {
        this.id = id;
        this.orderparent_id = orderparent_id;
        this.food_id = food_id;
        this.price = price;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
