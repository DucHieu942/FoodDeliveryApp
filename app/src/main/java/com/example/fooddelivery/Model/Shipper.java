package com.example.fooddelivery.Model;

public class Shipper {
    private String username;
    private String phonenumber;
    private String email;
    private String fullname;
    private String password;
    private String address;
    private String imgUrl;


    public Shipper() {
    }

    public Shipper(String username, String phonenumber, String email, String fullname, String password, String address, String imgUrl) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.address = address;
        this.imgUrl = imgUrl;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
