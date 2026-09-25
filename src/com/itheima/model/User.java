package com.itheima.model;

public class User {
    private int id;
    private String userName;
    private String password;
    private String phone;
    private String address;
    private int role;
    private String token;

    public User() {}

    public User(int id, String userName, String password, String phone, String address, int role, String token) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.token = token;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
