package com.itheima.model;

public class Cart {
    private int id;
    private int clothId;
    private Clothes clothes;
    private String clothSize;
    private int amount;
    private int userId;
    private User user;
    private String date;

    public Cart() {}

    public Cart(int clothId, int amount, int userId) {
        this.clothId = clothId;
        this.amount = amount;
        this.userId = userId;
    }

    public Cart(int id, int clothId, int amount, int userId, String date) {
        this.id = id;
        this.clothId = clothId;
        this.amount = amount;
        this.userId = userId;
        this.date = date;
    }

    public Cart(int clothId, String clothSize, int userId) {
        this.clothId = clothId;
        this.clothSize = clothSize;
        this.userId = userId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClothId() { return clothId; }
    public void setClothId(int clothId) { this.clothId = clothId; }

    public Clothes getClothes() { return clothes; }
    public void setClothes(Clothes clothes) { this.clothes = clothes; }

    public String getClothSize() { return clothSize; }
    public void setClothSize(String clothSize) { this.clothSize = clothSize; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    @Override
    public String toString() {
        return String.valueOf(id) + clothId + (clothes != null ? clothes.toString() : "null")
                + amount + userId + (user != null ? user.toString() : "null") + date;
    }
}
