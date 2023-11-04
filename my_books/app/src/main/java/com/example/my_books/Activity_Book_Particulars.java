package com.example.my_books;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class Activity_Book_Particulars extends Activity {
    String[] King_s=new String[]{"名著","小说","科幻","知识","历史","哲学"};//下拉框中填入的数据
    //                  作者      国籍      备注
    EditText book_name,author,nationality,comment,site_name;
    //        标题   提交
    TextView title,submit,date;
    //      种类
    Spinner king,site;
    //                    返回
    ImageView delete,like,back;
    sql sql;
    //得一个可写的SQLiteDatabase对象
    SQLiteDatabase db;
    //  模式
    int pattern,site_x,king_x;
    private Intent intent;
    book book,Cbook=new book();
    List<site> sites;
    String[] sites_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_particulars);
        initViews();

        //种类下拉框点击事件
        king.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //下拉框正在显示的数据给xlk
                king_x=(int)id;
                Cbook.setKing(king_x+1);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //地址下拉框点击事件
        site.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //下拉框正在显示的数据给xlk
                site_x=(int)id;
                if (site_x<sites.size()){
                    //设置属性为隐藏
                    site_name.setVisibility(View.GONE);
                }else {
                    site_name.setVisibility(View.VISIBLE);
                }
                Cbook.setSite(site_x);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //返回事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pattern==1){
                    Cbook.setBook_name(book_name.getText()+"");
                    Cbook.setAuthor(author.getText()+"");
                    Cbook.setNationality(nationality.getText()+"");
                    if (site_x==sites.size()){
                        AlertDialog aldg;//声明一个对话框
                        AlertDialog.Builder adBd=new AlertDialog.Builder(Activity_Book_Particulars.this);//实例化一个对话框格式
                        adBd.setMessage("检测到变更，是否应用变更");//给对话框格式设置文本
                        //给对话框格式设置按钮并设置其点击方法
                        adBd.setPositiveButton("应用变更", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Cbook.getSite_name()!=""){
                                    int id=(int) sql.add_site(db, Cbook.getSite_name());
                                    if (id>0){
                                        Cbook.setSite(id);
                                        if (Cbook.getBook_name()!=""&&Cbook.getAuthor()!=""&&Cbook.getNationality()!=""){
                                            //获取时间
                                            Date date1 = new Date();
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            Cbook.setDate(dateFormat.format(date1));
                                            if (sql.update_book(db,Cbook)==1){
                                                Toast.makeText(Activity_Book_Particulars.this, "修改成功", Toast.LENGTH_LONG).show();
                                                finish();
                                            }else {
                                                Toast.makeText(Activity_Book_Particulars.this, "修改失败", Toast.LENGTH_LONG).show();
                                            }
                                        }else{
                                            Toast.makeText(Activity_Book_Particulars.this, "请输入书名 作者 国籍", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(Activity_Book_Particulars.this, "添加书架失败", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(Activity_Book_Particulars.this, "书架名不能为空", Toast.LENGTH_LONG).show();
                                }
                            }
                        });//给对话框格式设置按钮并设置其点击方法
                        adBd.setNegativeButton("不应用变更", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();//返回上一级
                            }
                        });
                        adBd.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        aldg=adBd.create();//将对话框格式绑定到对话框
                        aldg.show();//生成对话框
                    }else{
                        Cbook.setSite(sites.get(site_x).getId());
                        if (!(Cbook.getBook_name().equals(book.getBook_name())&&
                                Cbook.getNationality().equals(book.getNationality())&&
                                Cbook.getAuthor().equals(book.getAuthor())&&
                                Cbook.getComment().equals(book.getComment())&&
                                Cbook.getKing()==book.getKing()&&Cbook.getLike()==book.getLike()&&
                                Cbook.getSite()==book.getSite())){
                            AlertDialog aldg;//声明一个对话框
                            AlertDialog.Builder adBd=new AlertDialog.Builder(Activity_Book_Particulars.this);//实例化一个对话框格式
                            adBd.setMessage("检测到变更，是否应用变更");//给对话框格式设置文本
                            //给对话框格式设置按钮并设置其点击方法
                            adBd.setPositiveButton("应用变更", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Cbook.getBook_name()!=""&&Cbook.getAuthor()!=""&&Cbook.getNationality()!=""){
                                        //获取时间
                                        Date date1 = new Date();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Cbook.setDate(dateFormat.format(date1));
                                        if (sql.update_book(db,Cbook)==1){
                                            Toast.makeText(Activity_Book_Particulars.this, "修改成功", Toast.LENGTH_LONG).show();
                                            finish();
                                        }else {
                                            Toast.makeText(Activity_Book_Particulars.this, "修改失败", Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(Activity_Book_Particulars.this, "请输入书名 作者 国籍", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });//给对话框格式设置按钮并设置其点击方法
                            adBd.setNegativeButton("不应用变更", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();//返回上一级
                                }
                            });
                            adBd.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            aldg=adBd.create();//将对话框格式绑定到对话框
                            aldg.show();//生成对话框
                        }else {
                            finish();//返回上一级
                        }
                    }
                }else {
                    finish();//返回上一级
                }

            }
        });
        //删除事件
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog aldg1;//声明一个对话框
                AlertDialog.Builder adBd1=new AlertDialog.Builder(Activity_Book_Particulars.this);//实例化一个对话框格式
                adBd1.setMessage("确定删除"+book.getBook_name()+"吗？");//给对话框格式设置文本
                //给对话框格式设置按钮并设置其点击方法
                adBd1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List< com.example.my_books.book > books = new LinkedList<book>();
                        books.add(book);
                        sql.sc_books(db,books);
                        finish();//返回上一级
                    }
                });
                //给对话框格式设置按钮并设置其点击方法
                adBd1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                aldg1=adBd1.create();//将对话框格式绑定到对话框
                aldg1.show();//生成对话框

            }
        });
        //收藏事件
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cbook.getLike()==0){
                    Cbook.setLike(1);
                    like.setImageResource(R.drawable.like_pitch);
                }else{
                    Cbook.setLike(0);
                    like.setImageResource(R.drawable.linke_pitch);
                }
            }
        });
        //确认事件
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pattern==1){
                    if (site_x==sites.size()){
                        Cbook.setSite_name(site_name.getText()+"");
                        if (Cbook.getSite_name()!=""){
                            int id=(int) sql.add_site(db, Cbook.getSite_name());
                            if (id>0){
                                Cbook.setSite(id);
                                Cbook.setBook_name(book_name.getText()+"");
                                Cbook.setAuthor(author.getText()+"");
                                Cbook.setNationality(nationality.getText()+"");
                                if (Cbook.getBook_name()!=""&&Cbook.getAuthor()!=""&&Cbook.getNationality()!=""){
                                    Cbook.setComment(comment.getText()+"");
                                    if (!(Cbook.getBook_name().equals(book.getBook_name())&&
                                            Cbook.getNationality().equals(book.getNationality())&&
                                            Cbook.getAuthor().equals(book.getAuthor())&&
                                            Cbook.getComment().equals(book.getComment())&&
                                            Cbook.getKing()==book.getKing()&&Cbook.getLike()==book.getLike()&&
                                            Cbook.getSite()==book.getSite())){
                                        AlertDialog aldg;//声明一个对话框
                                        AlertDialog.Builder adBd=new AlertDialog.Builder(Activity_Book_Particulars.this);//实例化一个对话框格式
                                        adBd.setMessage("确定保存"+Cbook.getBook_name()+"吗？");//给对话框格式设置文本
                                        //给对话框格式设置按钮并设置其点击方法
                                        adBd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //获取时间
                                                Date date1 = new Date();
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                Cbook.setDate(dateFormat.format(date1));
                                                if (sql.update_book(db,Cbook)==1){
                                                    Toast.makeText(Activity_Book_Particulars.this, "修改成功", Toast.LENGTH_LONG).show();
                                                    finish();
                                                }else {
                                                    Toast.makeText(Activity_Book_Particulars.this, "修改失败", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });//给对话框格式设置按钮并设置其点击方法
                                        adBd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        aldg=adBd.create();//将对话框格式绑定到对话框
                                        aldg.show();//生成对话框
                                    }else {
                                        finish();
                                    }
                                }else {
                                    Toast.makeText(Activity_Book_Particulars.this, "请输入书名 作者 国籍", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(Activity_Book_Particulars.this, "添加书架失败", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(Activity_Book_Particulars.this, "书架名不能为空", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Cbook.setSite(sites.get(site_x).getId());
                        Cbook.setBook_name(book_name.getText()+"");
                        Cbook.setAuthor(author.getText()+"");
                        Cbook.setNationality(nationality.getText()+"");
                        if (Cbook.getBook_name()!=""&&Cbook.getAuthor()!=""&&Cbook.getNationality()!=""){
                            Cbook.setComment(comment.getText()+"");
                            if (!(Cbook.getBook_name().equals(book.getBook_name())&&
                                    Cbook.getNationality().equals(book.getNationality())&&
                                    Cbook.getAuthor().equals(book.getAuthor())&&
                                    Cbook.getComment().equals(book.getComment())&&
                                    Cbook.getKing()==book.getKing()&&Cbook.getLike()==book.getLike()&&
                                    Cbook.getSite()==book.getSite())){
                                AlertDialog aldg;//声明一个对话框
                                AlertDialog.Builder adBd=new AlertDialog.Builder(Activity_Book_Particulars.this);//实例化一个对话框格式
                                adBd.setMessage("确定保存"+Cbook.getBook_name()+"吗？");//给对话框格式设置文本
                                //给对话框格式设置按钮并设置其点击方法
                                adBd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //获取时间
                                        Date date1 = new Date();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Cbook.setDate(dateFormat.format(date1));
                                        if (sql.update_book(db,Cbook)==1){
                                            Toast.makeText(Activity_Book_Particulars.this, "修改成功", Toast.LENGTH_LONG).show();
                                            finish();
                                        }else {
                                            Toast.makeText(Activity_Book_Particulars.this, "修改失败", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });//给对话框格式设置按钮并设置其点击方法
                                adBd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                aldg=adBd.create();//将对话框格式绑定到对话框
                                aldg.show();//生成对话框
                            }else {
                                finish();
                            }
                        }else {
                            Toast.makeText(Activity_Book_Particulars.this, "请输入书名 作者 国籍", Toast.LENGTH_LONG).show();
                        }
                    }
                }else {
                    if (site_x==sites.size()){
                        Cbook.setSite_name(site_name.getText()+"");
                        if (Cbook.getSite_name()!=""){
                            int id=(int) sql.add_site(db, Cbook.getSite_name());
                            if (id>0){
                                Cbook.setSite(id);
                                Cbook.setBook_name(book_name.getText()+"");
                                Cbook.setAuthor(author.getText()+"");
                                Cbook.setNationality(nationality.getText()+"");
                                if (Cbook.getBook_name()!=""&&Cbook.getAuthor()!=""&&Cbook.getNationality()!=""){
                                    Cbook.setComment(comment.getText()+"");
                                    AlertDialog aldg;//声明一个对话框
                                    AlertDialog.Builder adBd=new AlertDialog.Builder(Activity_Book_Particulars.this);//实例化一个对话框格式
                                    adBd.setMessage("确定添加"+Cbook.getBook_name()+"吗？");//给对话框格式设置文本
                                    //给对话框格式设置按钮并设置其点击方法
                                    adBd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //获取时间
                                            Date date1 = new Date();
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            Cbook.setDate(dateFormat.format(date1));
                                            if (sql.add_book(db,Cbook)>0){
                                                Toast.makeText(Activity_Book_Particulars.this, "添加成功", Toast.LENGTH_LONG).show();

                                                AlertDialog aldg;//声明一个对话框
                                                AlertDialog.Builder adBd=new AlertDialog.Builder(Activity_Book_Particulars.this);//实例化一个对话框格式
                                                adBd.setMessage("是否继续添加");//给对话框格式设置文本
                                                //给对话框格式设置按钮并设置其点击方法
                                                adBd.setPositiveButton("继续添加", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Cbook=new book();
                                                        book_name.setText(null);
                                                        author.setText(null);
                                                        comment.setText(null);
                                                        nationality.setText(null);
                                                        site_name.setText(null);
                                                        site.setSelection(0);
                                                        king.setSelection(0);
                                                        site_x=0;
                                                        king_x=0;
                                                        Cbook.setKing(king_x+1);
                                                        site_name.setVisibility(View.GONE);
                                                    }
                                                });//给对话框格式设置按钮并设置其点击方法
                                                adBd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                });
                                                aldg=adBd.create();//将对话框格式绑定到对话框
                                                aldg.show();//生成对话框
                                            }else {
                                                Toast.makeText(Activity_Book_Particulars.this, "添加失败", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });//给对话框格式设置按钮并设置其点击方法
                                    adBd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    aldg=adBd.create();//将对话框格式绑定到对话框
                                    aldg.show();//生成对话框

                                }else {
                                    Toast.makeText(Activity_Book_Particulars.this, "请输入书名 作者 国籍", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(Activity_Book_Particulars.this, "添加书架失败", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(Activity_Book_Particulars.this, "书架名不能为空", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Cbook.setSite(sites.get(site_x).getId());
                        Cbook.setBook_name(book_name.getText()+"");
                        Cbook.setAuthor(author.getText()+"");
                        Cbook.setNationality(nationality.getText()+"");
                        if (Cbook.getBook_name()!=""&&Cbook.getAuthor()!=""&&Cbook.getNationality()!=""){
                            Cbook.setComment(comment.getText()+"");
                            AlertDialog aldg;//声明一个对话框
                            AlertDialog.Builder adBd=new AlertDialog.Builder(Activity_Book_Particulars.this);//实例化一个对话框格式
                            adBd.setMessage("确定添加"+Cbook.getBook_name()+"吗？");//给对话框格式设置文本
                            //给对话框格式设置按钮并设置其点击方法
                            adBd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //获取时间
                                    Date date1 = new Date();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Cbook.setDate(dateFormat.format(date1));
                                    if (sql.add_book(db,Cbook)>0){
                                        Toast.makeText(Activity_Book_Particulars.this, "添加成功", Toast.LENGTH_LONG).show();

                                        AlertDialog aldg;//声明一个对话框
                                        AlertDialog.Builder adBd=new AlertDialog.Builder(Activity_Book_Particulars.this);//实例化一个对话框格式
                                        adBd.setMessage("是否继续添加");//给对话框格式设置文本
                                        //给对话框格式设置按钮并设置其点击方法
                                        adBd.setPositiveButton("继续添加", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Cbook=new book();
                                                book_name.setText(null);
                                                author.setText(null);
                                                comment.setText(null);
                                                nationality.setText(null);
                                                site_name.setText(null);
                                                site.setSelection(0);
                                                king.setSelection(0);
                                                site_x=0;
                                                king_x=0;
                                                Cbook.setKing(king_x+1);
                                                site_name.setVisibility(View.GONE);
                                            }
                                        });//给对话框格式设置按钮并设置其点击方法
                                        adBd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        aldg=adBd.create();//将对话框格式绑定到对话框
                                        aldg.show();//生成对话框
                                    }else {
                                        Toast.makeText(Activity_Book_Particulars.this, "添加失败", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });//给对话框格式设置按钮并设置其点击方法
                            adBd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            aldg=adBd.create();//将对话框格式绑定到对话框
                            aldg.show();//生成对话框

                        }else {
                            Toast.makeText(Activity_Book_Particulars.this, "请输入书名 作者 国籍", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private void initViews(){
        book_name=(EditText) findViewById(R.id.xq_xq_sm);
        author=(EditText) findViewById(R.id.xq_xq_zz);
        nationality=(EditText) findViewById(R.id.xq_xq_gj);
        comment=(EditText) findViewById(R.id.xq_xq_bz);
        site_name=(EditText) findViewById(R.id.xq_xq_cfd);
        title=(TextView) findViewById(R.id.xq_To_Xs);
        submit=(TextView) findViewById(R.id.xq_bc);
        date=(TextView) findViewById(R.id.xq_data);
        king=(Spinner) findViewById(R.id.xq_xq_zl);
        site=(Spinner) findViewById(R.id.xq_xq_cfd_sp);
        delete=(ImageView) findViewById(R.id.xq_sc);
        like=(ImageView) findViewById(R.id.xq_like);
        back=(ImageView) findViewById(R.id.xq_fh);
        //设置可见属性为显示
        //register.setVisibility(View.VISIBLE);
        //设置属性为隐藏
        site_name.setVisibility(View.GONE);
        sql=new sql(Activity_Book_Particulars.this);
        //得一个可写的SQLiteDatabase对象
        db= sql.getWritableDatabase();
        sites=sql.cxbj_site(db);
        sites_string=new String[sites.size()+1];
        for (int i = 0; i < sites.size(); i++) {
            sites_string[i]=sites.get(i).getSite();
        }
        sites_string[sites_string.length-1]="添加新书架";

        //实例化下拉框适配器并写入数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, King_s);
        //绑定适配器与下拉框
        king.setAdapter(adapter);
        //实例化下拉框适配器并写入数据
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sites_string);
        //绑定适配器与下拉框
        site.setAdapter(adapter1);

        intent=getIntent();//设置intent为接收模式
        pattern=(int) intent.getSerializableExtra("pattern");
        if (pattern==1){//查看模式
            book=new book((int) intent.getSerializableExtra("id"),
                    (String) intent.getSerializableExtra("book_name") ,
                    (String) intent.getSerializableExtra("author") ,
                    (String) intent.getSerializableExtra("nationality") ,
                    (int) intent.getSerializableExtra("king") ,
                    (String) intent.getSerializableExtra("comment") ,
                    (String) intent.getSerializableExtra("site_name") ,
                    (int) intent.getSerializableExtra("site") ,
                    (String) intent.getSerializableExtra("date") ,
                    (int) intent.getSerializableExtra("like") );
            Cbook=new book(book.getId(), book.getUser_id(), book.getBook_name(), book.getAuthor(),
                book.getNationality(),book.getKing(),book.getComment(),
                book.getSite(),book.getDate(),book.getLike());
            for (int i = 0; i < sites.size(); i++) {
                if (sites.get(i).getSite().equals(book.getSite_name())){
                    site_x=i;
                    break;
                }
            }
            king_x=book.getKing()-1;
            site.setSelection(site_x);
            king.setSelection(book.getKing()-1);
            title.setText(book.getBook_name());
            book_name.setText(book.getBook_name());
            author.setText(book.getAuthor());
            nationality.setText(book.getNationality());
            comment.setText(book.getComment());
            date.setText(book.getDate());
            if (book.getLike()==0){
                like.setImageResource(R.drawable.linke_pitch);
            }else{
                like.setImageResource(R.drawable.like_pitch);
            }
        }else {//添加模式
            delete.setVisibility(View.GONE);
            like.setVisibility(View.GONE);
            date.setVisibility(View.GONE);

        }
    }

}