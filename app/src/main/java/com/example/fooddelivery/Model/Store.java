package com.example.fooddelivery.Model;

public class Store {
    private String username;
    private String phonenumber;
    private String password;
    private String address;


    public Store() {
    }

    public Store(String username, String phonenumber, String password, String address) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.password = password;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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
}
