package com.example.my_books;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class sql extends SQLiteOpenHelper {
    String sql_user="user";//所有用户的库
    String user_use="user_use";//登录的用户
    String site="site";//所有用户的地址
    String books="book";//所有的书
    public sql(@Nullable Context context) {
        super(context,"sql.db", null, 1);
    }
    //默认建表方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+sql_user+" (id INTEGER PRIMARY KEY AUTOINCREMENT ,user_name TEXT,password TEXT)");
        db.execSQL("create table "+user_use+" (id INTEGER ,user_name TEXT,password TEXT)");
        db.execSQL("create table "+site+" (id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER,site TEXT)");
        //1.名著，2.小说，3.科幻，4.知识，5.历史，6.哲学
        db.execSQL("create table "+books+" (id INTEGER PRIMARY KEY AUTOINCREMENT ,user_id INTEGER,book_name TEXT,author TEXT,nationality TEXT,king INTEGER,comment TEXT,site INTEGER,date TEXT,likes INTEGER)");
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //以键值对的形式储存数据
        cValue.put("id",10001);
        cValue.put("user_name","root");
        cValue.put("password","123456");
        //调用insert()方法插入数据
        db.insert(sql_user,null,cValue);
        //实例化常量值
        ContentValues cValue1 = new ContentValues();
        //以键值对的形式储存数据
        cValue1.put("user_id",10001);
        cValue1.put("site","默认地址");
        cValue1.put("id",10001);
        //调用insert()方法插入数据
        db.insert(site,null,cValue1);
        //实例化常量值
        ContentValues cValue2 = new ContentValues();
        cValue2.put("id",10001);
        cValue2.put("user_id",10001);
        cValue2.put("book_name","Book_name");
        cValue2.put("author","Author");
        cValue2.put("nationality","Nationality");
        cValue2.put("king",1);
        cValue2.put("comment","Comment");
        cValue2.put("site",10001);
        cValue2.put("date","2012-10-28");
        cValue2.put("likes",1);
        //调用insert()方法插入数据
        db.insert(books,null,cValue2);
        //实例化常量值
        ContentValues cValue3= new ContentValues();
        cValue3.put("user_id",10001);
        cValue3.put("book_name","书名");
        cValue3.put("author","作者");
        cValue3.put("nationality","国籍");
        cValue3.put("king",2);
        cValue3.put("comment","备注");
        cValue3.put("site",10001);
        cValue3.put("date","2022-10-31");
        cValue3.put("likes",0);
        //调用insert()方法插入数据
        db.insert(books,null,cValue3);
    }
    //更新数据库方法
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }
    //登录查询方法
    public List<user> cxdl(SQLiteDatabase db,String account){
        List<user> list_user=new LinkedList<>();
        //按cxl和cxtj来查询sbm中的数据并储存到cursor中
        Cursor cursor=db.query( sql_user,null,"user_name = ?",new String[]{account},null,null,"id desc");
        //判断cursor是否为空
        if (cursor.moveToFirst()){
            //用do while来遍历cursor
            do {
                //实例化一个有数据的shu类型，参数来自按列查询cursor的数据
                com.example.my_books.user user =new user(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("user_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")));
                //把shu添加到链表中
                list_user.add(user);
                //根据cursor是否有下一条数据来决定是否继续循环
            }while (cursor.moveToNext());
        }
        //关闭cursor
        cursor.close();
        return list_user;
    }
    //本机查询方法
    public user cxbj(SQLiteDatabase db){
        user user=new user();
        //按cxl和cxtj来查询sbm中的数据并储存到cursor中
        Cursor cursor=db.query( user_use,null,null,null,null,null,"id desc");
        //判断cursor是否为空
        if (cursor.moveToFirst()){
            //用do while来遍历cursor
            do {
                //实例化一个有数据的shu类型，参数来自按列查询cursor的数据
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                user.setUser_name(cursor.getString(cursor.getColumnIndexOrThrow("user_name")));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                //根据cursor是否有下一条数据来决定是否继续循环
            }while (cursor.moveToNext());
        }
        //关闭cursor
        cursor.close();
        return user;
    }
    //更新本机用户数据
    public void gxbj(SQLiteDatabase db,user user){
        //删除本机用户数据
        scbj(db);
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //以键值对的形式储存数据
        cValue.put("id",user.getId());
        cValue.put("user_name",user.getUser_name());
        cValue.put("password",user.getPassword());
        //调用insert()方法插入数据
        db.insert(user_use,null,cValue);
    }
    //删除本机用户数据
    public void scbj(SQLiteDatabase db){
        db.delete(user_use,null,null);
    }
    //注册用户      注册成功返回id，失败返回-1
    public long zcyh(SQLiteDatabase db,user user){
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //以键值对的形式储存数据
        cValue.put("user_name",user.getUser_name());
//        cValue.put("account",user.getAccount());
        cValue.put("password",user.getPassword());
        //调用insert()方法插入数据
        user.setId((int)db.insert(sql_user,null,cValue));  ;
        if (user.getId()!=0){
            gxbj(db, user);
            add_site(db, "默认地址");
        }
        return user.getId();
    }
    //添加书籍
    public long add_book(SQLiteDatabase db,book book){
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //id  ,user_id INTEGER,book_name TEXT,author TEXT,
        // nationality TEXT,king INTEGER,comment TEXT,
        // site INTEGER,date TEXT,likes INTEGER
        user user=cxbj(db);
        //以键值对的形式储存数据
        cValue.put("user_id",user.getId());
        cValue.put("book_name",book.getBook_name());
        cValue.put("author",book.getAuthor());
        cValue.put("nationality",book.getNationality());
        cValue.put("king",book.getKing());
        cValue.put("comment",book.getComment());
        cValue.put("site",book.getSite());
        cValue.put("date",book.getDate());
        cValue.put("likes",book.getLike());
        //调用insert()方法插入数据
        return db.insert(books,null,cValue);
    }
    //添加地址
    public long add_site(SQLiteDatabase db,String site1){
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //id INTEGER PRIMARY KEY AUTOINCREMENT,
        // user_id INTEGER,site TEXT
        user user=cxbj(db);
        //以键值对的形式储存数据
        cValue.put("user_id",user.getId());
        cValue.put("site",site1);
        //调用insert()方法插入数据
        return db.insert(site,null,cValue);
    }
    //查询地址
    public  List<site> cxbj_site(SQLiteDatabase db){
        List<site> list_site=new LinkedList<>();
        //按cxl和cxtj来查询sbm中的数据并储存到cursor中
        Cursor cursor=db.query( site,null,"user_id = ?",new String[]{String.valueOf(cxbj(db).getId())},null,null,"id desc");
        //判断cursor是否为空
        if (cursor.moveToFirst()){
            //用do while来遍历cursor
            do {
                //实例化一个有数据的shu类型，参数来自按列查询cursor的数据
                //把shu添加到链表中
                site site1 =new site(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("site")));
                list_site.add(site1);
                //根据cursor是否有下一条数据来决定是否继续循环
            }while (cursor.moveToNext());
        }
        //关闭cursor
        cursor.close();
        return list_site;
    }
    //查询地址其中user_name替换为了该书架的数量
    public  List<site> cxbj_site_quantity(SQLiteDatabase db){
        List<site> site1 = cxbj_site(db);
        List<book> books1= cxbj_books(db);
        for (site a:site1) {
            a.setUser_name(0);
        }
        for (book a:books1) {
            for (site a1:site1) {
                if (a1.getId()==a.getSite()){
                    a1.setUser_name(a1.getUser_name()+1);
                }
            }
        }
        return site1;
    }
    //查询书籍
    public List<book> cxbj_books(SQLiteDatabase db){
        List<book> list_book=new LinkedList<>();
        //按cxl和cxtj来查询sbm中的数据并储存到cursor中
        Cursor cursor=db.query( books,null,"user_id = ?",new String[]{String.valueOf(cxbj(db).getId())},null,null,"date desc");
        //判断cursor是否为空
        if (cursor.moveToFirst()){
            //用do while来遍历cursor
            do {
                //实例化一个有数据的shu类型，参数来自按列查询cursor的数据
                //把shu添加到链表中
                //id INTEGER PRIMARY KEY AUTOINCREMENT ,user_id INTEGER,book_name TEXT,
                // author TEXT,nationality TEXT,king INTEGER,comment TEXT,
                // site INTEGER,date TEXT,likes INTEGER

                //int id,int user_id,String book_name,String author,
                // String nationality,int king,String comment,int site,String date,int like
                book book =new book(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("book_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("author")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nationality")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("king")),
                        cursor.getString(cursor.getColumnIndexOrThrow("comment")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("site")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("likes")));

                list_book.add(book);
                //根据cursor是否有下一条数据来决定是否继续循环
            }while (cursor.moveToNext());
        }
        //关闭cursor
        cursor.close();
        return list_book;
    }
    //查询包含地址信息的书籍
    public List<book> cxbj_intact_s_book(SQLiteDatabase db,String content,List<site> bj_site,int sort,Boolean[] p){
        List<book> book = cxbj_books_S(db,bj_site,content,sort,p);
        for (book book1:book) {
            for (site site:bj_site) {
                if (book1.getSite()==site.getId()){
                    book1.setSite_name(site.getSite());
                    break;
                }
            }
        }
        return book;
    }
    //查询书籍
    public List<book> cxbj_books_S(SQLiteDatabase db,List<site> sites,String content,int sort,Boolean[] p){
        String selection="user_id = ?",orderBy;
        List<String> L_selectionArgsnew =new ArrayList<String>();
        L_selectionArgsnew.add(String.valueOf(cxbj(db).getId()));

        if (sort==0){
            orderBy="date desc";
        }else if (sort==1){
            orderBy="date";
        }else if (sort==2){
            orderBy="id desc";
        }else {
            orderBy="id";
        }
//        id INTEGER PRIMARY KEY AUTOINCREMENT ,
//        user_id INTEGER,
//        book_name TEXT,author TEXT,nationality TEXT,king INTEGER,
//        comment TEXT,site INTEGER,date TEXT,likes INTEGER)");
        if (!content.equals("")){
            selection=selection+" and (";
            boolean p1 = false;
            if (p[0]){
                selection=selection+" book_name like ?";
                L_selectionArgsnew.add("%"+content+"%");
                p1=true;
            }
            if (p[1]){
                if (p1){
                    selection=selection+" or author like ?";
                    L_selectionArgsnew.add("%"+content+"%");
                }else {
                    selection=selection+" author like ?";
                    L_selectionArgsnew.add("%"+content+"%");
                    p1=true;
                }
            }
            if (p[2]){
                if (p1){
                    selection=selection+" or nationality like ?";
                    L_selectionArgsnew.add("%"+content+"%");
                }else {
                    selection=selection+" nationality like ?";
                    L_selectionArgsnew.add("%"+content+"%");
                    p1=true;
                }
            }
            if (p[3]){
                if (p1){
                    selection=selection+" or comment like ?";
                    L_selectionArgsnew.add("%"+content+"%");
                }else {
                    selection=selection+" comment like ?";
                    L_selectionArgsnew.add("%"+content+"%");
                    p1=true;
                }
            }
            selection=selection+" )";
        }
        //1.名著，2.小说，3.科幻，4.知识，5.历史，6.哲学
        if (!(p[4]&&p[5]&&p[6]&&p[7]&&p[8]&&p[9])){
            selection=selection+" and (";
            boolean p1 = false;
            if (p[4]){
                selection=selection+" king = ?";
                L_selectionArgsnew.add("2");
                p1=true;
            }
            if (p[5]){
                if (p1){
                    selection=selection+" or king = ?";
                    L_selectionArgsnew.add("1");
                }else {
                    selection=selection+" king = ?";
                    L_selectionArgsnew.add("1");
                    p1=true;
                }
            }
            if (p[6]){
                if (p1){
                    selection=selection+" or king = ?";
                    L_selectionArgsnew.add("3");
                }else {
                    selection=selection+" king = ?";
                    L_selectionArgsnew.add("3");
                    p1=true;
                }
            }
            if (p[7]){
                if (p1){
                    selection=selection+" or king = ?";
                    L_selectionArgsnew.add("4");
                }else {
                    selection=selection+" king = ?";
                    L_selectionArgsnew.add("4");
                    p1=true;
                }
            }
            if (p[8]){
                if (p1){
                    selection=selection+" or king = ?";
                    L_selectionArgsnew.add("5");
                }else {
                    selection=selection+" king = ?";
                    L_selectionArgsnew.add("5");
                    p1=true;
                }
            }
            if (p[9]){
                if (p1){
                    selection=selection+" or king = ?";
                    L_selectionArgsnew.add("6");
                }else {
                    selection=selection+" king = ?";
                    L_selectionArgsnew.add("6");
                    p1=true;
                }
            }
            selection=selection+" )";
        }
        if (p[10]){
            selection=selection+" and likes = ?";
            L_selectionArgsnew.add("1");
        }
        boolean p1=false;
        for (site site:sites) {
            if (!site.getP()){
                p1=true;
                break;
            }
        }
        if (p1){
            selection=selection+" and (";
            for (site site:sites) {
                if (site.getP()){
                    if (p1){
                        selection=selection+" site = ?";
                        L_selectionArgsnew.add(String.valueOf(site.getId()));
                        p1=!p1;
                    }else {
                        selection=selection+" or site = ?";
                        L_selectionArgsnew.add(String.valueOf(site.getId()));
                    }
                }
            }
            selection=selection+" )";
        }
        List<book> list_book=new LinkedList<>();
        String[] selectionArgsnew=new String[L_selectionArgsnew.size()];
        for (int i = 0; i < L_selectionArgsnew.size(); i++) {
            selectionArgsnew[i]=L_selectionArgsnew.get(i);
        }
        //按cxl和cxtj来查询sbm中的数据并储存到cursor中
        Cursor cursor=db.query(books,null,selection,
                selectionArgsnew,
                null,null,orderBy);
        //判断cursor是否为空
        if (cursor.moveToFirst()){
            //用do while来遍历cursor
            do {
                //实例化一个有数据的shu类型，参数来自按列查询cursor的数据
                //把shu添加到链表中
                //id INTEGER PRIMARY KEY AUTOINCREMENT ,user_id INTEGER,book_name TEXT,
                // author TEXT,nationality TEXT,king INTEGER,comment TEXT,
                // site INTEGER,date TEXT,likes INTEGER

                //int id,int user_id,String book_name,String author,
                // String nationality,int king,String comment,int site,String date,int like
                book book =new book(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("book_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("author")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nationality")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("king")),
                        cursor.getString(cursor.getColumnIndexOrThrow("comment")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("site")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("likes")));

                list_book.add(book);
                //根据cursor是否有下一条数据来决定是否继续循环
            }while (cursor.moveToNext());
        }
        //关闭cursor
        cursor.close();
        return list_book;
    }
    //查询指定书架书籍
    public List<book> cxsite_book(SQLiteDatabase db,int site_id){
        List<book> list_book=new LinkedList<>();
        //按cxl和cxtj来查询sbm中的数据并储存到cursor中
        Cursor cursor=db.query( books,null,"site = ?",new String[]{String.valueOf(site_id)},null,null,"id desc");
        //判断cursor是否为空
        if (cursor.moveToFirst()){
            //用do while来遍历cursor
            do {
                //实例化一个有数据的shu类型，参数来自按列查询cursor的数据
                //把shu添加到链表中
                //id INTEGER PRIMARY KEY AUTOINCREMENT ,user_id INTEGER,book_name TEXT,
                // author TEXT,nationality TEXT,king INTEGER,comment TEXT,
                // site INTEGER,date TEXT,likes INTEGER

                //int id,int user_id,String book_name,String author,
                // String nationality,int king,String comment,int site,String date,int like
                book book =new book(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("book_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("author")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nationality")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("king")),
                        cursor.getString(cursor.getColumnIndexOrThrow("comment")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("site")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("likes")));

                list_book.add(book);
                //根据cursor是否有下一条数据来决定是否继续循环
            }while (cursor.moveToNext());
        }
        //关闭cursor
        cursor.close();
        return list_book;
    }
    //查询本机所有种类各自的数量
    public int[] cxbj_book_AllKing_amount(SQLiteDatabase db) {
        return new int[]{db.query(books, null, "king = ? and user_id = ?", new String[]{"1",String.valueOf(cxbj(db).getId())}, null, null, "id desc").getCount(),
                db.query(books, null, "king = ? and user_id = ?", new String[]{"2",String.valueOf(cxbj(db).getId())}, null, null, "id desc").getCount(),
                db.query(books, null, "king = ? and user_id = ?", new String[]{"3",String.valueOf(cxbj(db).getId())}, null, null, "id desc").getCount(),
                db.query(books, null, "king = ? and user_id = ?", new String[]{"4",String.valueOf(cxbj(db).getId())}, null, null, "id desc").getCount(),
                db.query(books, null, "king = ? and user_id = ?", new String[]{"5",String.valueOf(cxbj(db).getId())}, null, null, "id desc").getCount(),
                db.query(books, null, "king = ? and user_id = ?", new String[]{"6",String.valueOf(cxbj(db).getId())}, null, null, "id desc").getCount()};
    }
    //查询本机书籍与书架数量
    public int[] cxbj_book_site_amount(SQLiteDatabase db) {
        return new int[]{db.query(books, null, "user_id = ?", new String[]{String.valueOf(cxbj(db).getId())}, null, null, "id desc").getCount(),
                db.query(site, null, "user_id = ?", new String[]{String.valueOf(cxbj(db).getId())}, null, null, "id desc").getCount()};
    }
    //查询包含地址信息的书籍
    public List<book> cxbj_intact_book(SQLiteDatabase db,List<site> bj_site){
        List<book> book = cxbj_books(db);
        for (book book1:book) {
            for (site site:bj_site) {
                if (book1.getSite()==site.getId()){
                    book1.setSite_name(site.getSite());
                    break;
                }
            }
        }
        return book;
    }
    //删除书籍 返回更改条目-1为失败
    public int sc_books(SQLiteDatabase db,List<book> bookList){
        int a=0;
        //删除条件
        String whereClause = "id=?";
        //删除条件参数
        String[] whereArgs =new String[1];
        for (int i = 0; i < bookList.size(); i++) {
            whereArgs[0]=String.valueOf(bookList.get(i).getId());
            if (db.delete(books,whereClause,whereArgs)>-1){
                a++;
            }
        }
        return a;
    }
    //更新书籍
    public long update_book(SQLiteDatabase db,book book){
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //id  ,user_id INTEGER,book_name TEXT,author TEXT,
        // nationality TEXT,king INTEGER,comment TEXT,
        // site INTEGER,date TEXT,likes INTEGER
        user user=cxbj(db);
        //以键值对的形式储存数据
        cValue.put("user_id",user.getId());
        cValue.put("book_name",book.getBook_name());
        cValue.put("author",book.getAuthor());
        cValue.put("nationality",book.getNationality());
        cValue.put("king",book.getKing());
        cValue.put("comment",book.getComment());
        cValue.put("site",book.getSite());
        cValue.put("date",book.getDate());
        cValue.put("likes",book.getLike());
        return db.update(books,cValue,"id=?",new String[] {String.valueOf(book.getId())});
    }
    //更新书籍
    public long update_book(SQLiteDatabase db,List<book> book){
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //id  ,user_id INTEGER,book_name TEXT,author TEXT,
        // nationality TEXT,king INTEGER,comment TEXT,
        // site INTEGER,date TEXT,likes INTEGER
        user user=cxbj(db);
        int i=0;
        for (book book1:book) {
            //以键值对的形式储存数据
            cValue.put("user_id",user.getId());
            cValue.put("book_name",book1.getBook_name());
            cValue.put("author",book1.getAuthor());
            cValue.put("nationality",book1.getNationality());
            cValue.put("king",book1.getKing());
            cValue.put("comment",book1.getComment());
            cValue.put("site",book1.getSite());
            cValue.put("date",book1.getDate());
            cValue.put("likes",book1.getLike());
            if (db.update(books,cValue,"id=?",new String[] {String.valueOf(book1.getId())})>0){
                i++;
            }
        }

        return i;
    }
    //更新书架
    public int update_site(SQLiteDatabase db,site site){
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //id  ,user_id INTEGER,book_name TEXT,author TEXT,
        // nationality TEXT,king INTEGER,comment TEXT,
        // site INTEGER,date TEXT,likes INTEGER
        user user=cxbj(db);
        //以键值对的形式储存数据
        cValue.put("user_id",user.getId());
        cValue.put("site",site.getSite());
        return db.update(this.site,cValue,"id=?",new String[] {String.valueOf(site.getId())});
    }
    //删除书籍 返回更改条目-1为失败
    public int sc_site(SQLiteDatabase db,site site){
        List<book> books=cxbj_books(db);
        for (int i = books.size()-1; i >-1 ; i--) {
            if (books.get(i).getSite()!=site.getId()){
                books.remove(i);
            }
        }
        if (books.size()!=0){
            sc_books(db,books);
        }
        //删除条件
        String whereClause = "id=?";
        //删除条件参数
        String[] whereArgs ={String.valueOf(site.getId())};
        return db.delete(this.site,whereClause,whereArgs);
    }
}
