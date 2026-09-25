package com.itheima.model;

import java.math.BigDecimal;

public class Order {
    private int id;
    private String clothesDetails;
    private BigDecimal price;
    private String status;
    private int userId;
    private User user;
    private String address;
    private String time;

    public Order() {}

    public Order(int id, String clothesDetails, BigDecimal price, String status,
                 int userId, User user, String address, String time) {
        this.id = id;
        this.clothesDetails = clothesDetails;
        this.price = price;
        this.status = status;
        this.userId = userId;
        this.user = user;
        this.address = address;
        this.time = time;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getClothesDetails() { return clothesDetails; }
    public void setClothesDetails(String clothesDetails) { this.clothesDetails = clothesDetails; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
