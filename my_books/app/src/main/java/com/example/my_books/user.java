package com.example.my_books;

public class user {
    private int id;
    private String user_name;
    //    密码
    private String password;
    public user(){}
    public user(String user_name,String password){
        this.user_name=user_name;
        this.password=password;
    }
    public user(int id,String user_name,String password){
        this.id=id;
        this.user_name=user_name;
        this.password=password;
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
class book{
    private int id;
    private int user_id;
    private String book_name;
    //作者
    private String author;
    //    国籍
    private String nationality;
    //1.名著，2.小说，3.科幻，4.知识，5.历史，6.哲学
    private int king;
    private String comment;
    private int site;
    private String site_name;
    private String date;
    private int like;
    private boolean p;
    public book(){}
    public book(int id,int user_id,String book_name,String author,String nationality,int king,String comment,int site,String date,int like){
        this.id=id;
        this.user_id=user_id;
        this.book_name=book_name;
        this.author=author;
        this.nationality=nationality;
        this.king=king;
        this.comment=comment;
        this.site=site;
        this.date=date;
        this.like=like;
    }
    public book(int id,int user_id,String book_name,String author,String nationality,int king,String comment,String site_name,int site,String date,int like){
        this.id=id;
        this.user_id=user_id;
        this.book_name=book_name;
        this.author=author;
        this.nationality=nationality;
        this.king=king;
        this.comment=comment;
        this.site_name=site_name;
        this.site=site;
        this.date=date;
        this.like=like;
    }
    public book(int id,String book_name,String author,String nationality,int king,String comment,String site_name,int site,String date,int like){
        this.id=id;
        this.book_name=book_name;
        this.author=author;
        this.nationality=nationality;
        this.king=king;
        this.comment=comment;
        this.site_name=site_name;
        this.site=site;
        this.date=date;
        this.like=like;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getKing() {
        return king;
    }

    public void setKing(int king) {
        this.king = king;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public boolean getP() {
        return p;
    }

    public void setP(boolean p) {
        this.p = p;
    }
}
class site{
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