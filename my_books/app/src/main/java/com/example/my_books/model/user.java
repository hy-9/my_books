package com.example.my_books.model;

public class user {
    private int id;
    private String user_name;
    //    密码
    private String password;

    public user() {
    }

    public user(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    public user(int id, String user_name, String password) {
        this.id = id;
        this.user_name = user_name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
