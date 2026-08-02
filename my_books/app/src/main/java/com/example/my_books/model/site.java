package com.example.my_books.model;

public class site{
    private int id;
    private int user_name;
    private String site;
    private boolean p;
    public site(){p=false;}
    public site(int id,int user_name,String site){
        this.id=id;
        this.user_name=user_name;
        this.site=site;
        p=false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_name() {
        return user_name;
    }

    public void setUser_name(int user_name) {
        this.user_name = user_name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public boolean getP(){return p;}

    public void setP(boolean p) {
        this.p = p;
    }
}
