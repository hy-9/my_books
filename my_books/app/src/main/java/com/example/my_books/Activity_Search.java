package com.example.my_books;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Activity_Search extends Activity {
//       排列填充
String[] sort_S={"时间升序","时间降序","编号升序","编号降序"};
//                               检索图标
ImageView back,Call,delete,more,searching;
//      检索输入框
EditText search;
//            选择显示,筛选
TextView Cback,Cshow,screen,Cmove,Clike,Sback,Sscope_all,Sking_all,Ssite_all,Slike,Ssubmit;
CheckBox CBbook_name,CBauthor,CBnationality,CBcomment,CBxs,CBmz,CBkh,CBzs,CBls,CBzx;
ListView bookLV,siteLV;
RelativeLayout Sbackground,Sui,Cui,Cback_ui,Cmore;
Spinner sort;
sql sql;
SQLiteDatabase db;
List<book> books,Cbook=new ArrayList<book>();
shu_spq spq;//书模板的适配器
site_spq spq_s;//书模板的适配器
Boolean Cpattern=false,CmoreP=false;
int sorts=0;
List<site> sites;
//             书名   作者 国籍 备注  小说 名著  科幻 知识  历史  哲学  收藏
Boolean[] Cbx={true,true,true,true,true,true,true,true,true,true,false};
//      关键字
String keyword="";
private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
//        update();

        //返回点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //筛选点击事件
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_ui(-2,null);
            }
        });
        //排序点击事件
        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                sorts=(int)id;
                update_book();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //筛选背景点击事件
        Sbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_ui(-3,null);
            }
        });
        //筛选返回点击事件
        Sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_ui(-3,null);
            }
        });
        //筛选确认事件
        Ssubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_ui(-3,null);
            }
        });
        //多选返回事件
        Cback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_ui(-3,null);
            }
        });
        //书的长按事件
        bookLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Cpattern&&Sui.getVisibility()==View.GONE){
                    book cshu=(book)adapterView.getItemAtPosition(i);//把点击的那条数据包装到cshu里
                    update_ui(i,cshu);//更改背景方法
                }
                return false;
            }
        });
        //书的点击事件会跳转到书的详细界面
        bookLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                book cshu=(book) adapterView.getItemAtPosition(i);//把点击的那条数据包装到cshu里
                if (!Cpattern&&Sui.getVisibility()==View.GONE){
                    Intent intent=new Intent(Activity_Search.this,Activity_Book_Particulars.class);//设置跳转
                    //把数据传递   String book_name,String author,String nationality,int king,String comment,
                    //              int site,String date,int like

                    intent.putExtra("book_name",cshu.getBook_name());
                    intent.putExtra("id",cshu.getId());
                    intent.putExtra("author",cshu.getAuthor());
                    intent.putExtra("nationality",cshu.getNationality());
                    intent.putExtra("king",cshu.getKing());
                    intent.putExtra("comment",cshu.getComment());
                    intent.putExtra("site",cshu.getSite());
                    intent.putExtra("site_name",cshu.getSite_name());
                    intent.putExtra("date",cshu.getDate());
                    intent.putExtra("like",cshu.getLike());
                    intent.putExtra("pattern",1);
                    startActivity(intent);//执行跳转
                }else if (Sui.getVisibility()==View.GONE){
                    update_ui(i,cshu);
                }
            }
        });
        //多选全选事件
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_ui(-4,null);
            }
        });
        //多选删除事件
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cbook.size()==0){
                    Toast.makeText(Activity_Search.this,"请选择要删除的书",Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog aldg1;//声明一个对话框
                    AlertDialog.Builder adBd1=new AlertDialog.Builder(Activity_Search.this);//实例化一个对话框格式
                    adBd1.setMessage("确定删除这"+Cbook.size()+"本书吗？");//给对话框格式设置文本
                    //给对话框格式设置按钮并设置其点击方法
                    adBd1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean p1=true;
                            int a=Cbook.size();
                            if (sql.sc_books(db,Cbook)<-1){//删除数据若失败更改p1
                                p1=false;
                            }
                            update_ui(-3,null);
                            if (p1){
                                Toast.makeText(Activity_Search.this,"以删除选中的"+a+"本书",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Activity_Search.this,"删除失败 未知错误",Toast.LENGTH_SHORT).show();
                            }
                            update_book();
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
            }
        });
        //多选更多事件
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_ui(-1,null);
            }
        });
        //多选移动事件
        Cmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] sj=new String[sites.size()];
                int i=0;
                for (site site:sites) {
                    sj[i]=site.getSite();
                    i++;
                }
                Spinner inputServer=new Spinner(Activity_Search.this);;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Activity_Search.this,android.R.layout.simple_spinner_item,sj);
                inputServer.setAdapter(adapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Search.this);
                builder.setTitle("请选择移动至书架");
                builder.setView(inputServer);
                builder.setPositiveButton("确定" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int j=(int) inputServer.getSelectedItemId();
                        j=sites.get(j).getId();
                        Date date1 = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        for (book b:Cbook) {
                            b.setSite(j);
                            b.setDate(dateFormat.format(date1));
                        }
                        int a=(int) sql.update_book(db,Cbook);
                        Toast.makeText(Activity_Search.this, "已将"+a+"本书籍移动", Toast.LENGTH_LONG).show();
                        update_ui(-3,null);
                    }
                });
                //给对话框格式设置按钮并设置其点击方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        //多选收藏事件
        Clike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog aldg1;//声明一个对话框
                AlertDialog.Builder adBd1=new AlertDialog.Builder(Activity_Search.this);//实例化一个对话框格式
                adBd1.setMessage("确定收藏这"+Cbook.size()+"本书吗？");//给对话框格式设置文本
                //给对话框格式设置按钮并设置其点击方法
                adBd1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean p1=true;
                        int a=Cbook.size();
                        //获取时间
                        Date date1 = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        for (book book:Cbook) {
                            book.setLike(1);
                            book.setDate(dateFormat.format(date1));
                        }
                        if (sql.update_book(db,Cbook)<-1){//删除数据若失败更改p1
                            p1=false;
                        }
                        update_ui(-3,null);
                        if (p1){
                            Toast.makeText(Activity_Search.this,"收藏成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Activity_Search.this,"收藏失败 未知错误",Toast.LENGTH_SHORT).show();
                        }
                        update_book();
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
        //检索范围全选事件
        Sscope_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cbx[0]=true;
                Cbx[1]=true;
                Cbx[2]=true;
                Cbx[3]=true;
                CBbook_name.setChecked(true);
                CBauthor.setChecked(true);
                CBnationality.setChecked(true);
                CBcomment.setChecked(true);
                update_book();
            }
        });
        //检索范围书名事件
        CBbook_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cbx[0]||(Cbx[1]||Cbx[2]||Cbx[3])){
                    Cbx[0]=!Cbx[0];
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                CBbook_name.setChecked(Cbx[0]);
            }
        });
        //检索范围作者事件
        CBauthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cbx[1]||(Cbx[0]||Cbx[2]||Cbx[3])){
                    Cbx[1]=!Cbx[1];
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                CBauthor.setChecked(Cbx[1]);
            }
        });
        //检索范围国籍事件
        CBnationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cbx[2]||(Cbx[1]||Cbx[0]||Cbx[3])){
                    Cbx[2]=!Cbx[2];
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                CBnationality.setChecked(Cbx[2]);
            }
        });
        //检索范围备注事件
        CBcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cbx[3]||(Cbx[1]||Cbx[2]||Cbx[0])){
                    Cbx[3]=!Cbx[3];
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                CBcomment.setChecked(Cbx[3]);
            }
        });
        //检索种类全选事件
        Sking_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cbx[4]=true;
                Cbx[5]=true;
                Cbx[6]=true;
                Cbx[7]=true;
                Cbx[8]=true;
                Cbx[9]=true;
                CBxs.setChecked(Cbx[4]);
                CBmz.setChecked(Cbx[5]);
                CBkh.setChecked(Cbx[6]);
                CBzs.setChecked(Cbx[7]);
                CBls.setChecked(Cbx[8]);
                CBzx.setChecked(Cbx[9]);
                update_book();
            }
        });
        //检索种类小说事件
        CBxs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cbx[4]||(Cbx[5]||Cbx[6]||Cbx[7]||Cbx[8]||Cbx[9])){
                    Cbx[4]=!Cbx[4];
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                CBxs.setChecked(Cbx[4]);
            }
        });
        //检索种类名著事件
        CBmz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cbx[5]||(Cbx[4]||Cbx[6]||Cbx[7]||Cbx[8]||Cbx[9])){
                    Cbx[5]=!Cbx[5];
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                CBmz.setChecked(Cbx[5]);
            }
        });
        //检索种类科幻事件
        CBkh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cbx[6]||(Cbx[4]||Cbx[5]||Cbx[7]||Cbx[8]||Cbx[9])){
                    Cbx[6]=!Cbx[6];
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                CBkh.setChecked(Cbx[6]);
            }
        });
        //检索种类知识事件
        CBzs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cbx[7]||(Cbx[4]||Cbx[5]||Cbx[6]||Cbx[8]||Cbx[9])){
                    Cbx[7]=!Cbx[7];
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                CBzs.setChecked(Cbx[7]);
            }
        });
        //检索种类历史事件
        CBls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cbx[8]||(Cbx[4]||Cbx[5]||Cbx[6]||Cbx[7]||Cbx[9])){
                    Cbx[8]=!Cbx[8];
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                CBls.setChecked(Cbx[8]);
            }
        });
        //检索种类哲学事件
        CBzx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cbx[9]||(Cbx[4]||Cbx[5]||Cbx[6]||Cbx[7]||Cbx[8])){
                    Cbx[9]=!Cbx[9];
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                CBzx.setChecked(Cbx[9]);
            }
        });
        //检索事件
        searching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_book();
            }
        });
        //检索收藏事件
        Slike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cbx[10]){
                    Slike.setBackgroundResource(R.drawable.yuan_jiao_g);
                }else {
                    Slike.setBackgroundResource(R.drawable.yuan_jiao_blue);
                }
                Cbx[10]=!Cbx[10];
                update_book();
            }
        });
        //检索书架全选事件
        Ssite_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (site site:sites) {
                    site.setP(true);
                }
                //实例化适配器   上下文 ，数据链表;
                spq_s = new site_spq(Activity_Search.this,sites,false);
                //根据适配器生成书
                siteLV.setAdapter(spq_s);
                spq_s.notifyDataSetChanged();//更新适配器
                update_book();
            }
        });
        //书架点击事件
        siteLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean pp =false;
                for (int i = 0; i < sites.size(); i++) {
                    if (i!=position){
                        if (pp||sites.get(i).getP()){
                            pp=true;
                            break;
                        }
                    }
                }
                if (!sites.get(position).getP()||pp){
                    sites.get(position).setP(!sites.get(position).getP());
                    update_book();
                }else {
                    Toast.makeText(Activity_Search.this, "至少需存在一个选项", Toast.LENGTH_LONG).show();
                }
                //实例化适配器   上下文 ，数据链表;
                spq_s = new site_spq(Activity_Search.this,sites,false);
                //根据适配器生成书
                siteLV.setAdapter(spq_s);
                spq_s.notifyDataSetChanged();//更新适配器
            }
        });
    }

    private void update_book() {
        keyword=search.getText().toString();
        books=sql.cxbj_intact_s_book(db,keyword,sites,sorts,Cbx);
        //实例化适配器   上下文 ，数据链表;
        spq = new shu_spq(Activity_Search.this,books);
        //根据适配器生成书
        bookLV.setAdapter(spq);
        spq.notifyDataSetChanged();//更新适配器
    }
    private void update_ui(int p,book cshu){
        if (p==-1){
            if (CmoreP){
                Cmore.setVisibility(View.GONE);
                more.setImageResource(R.drawable.more);
            }else {
                Cmore.setVisibility(View.VISIBLE);
                more.setImageResource(R.drawable.more_1);
            }
            CmoreP=!CmoreP;
        }else if (p==-2){//显示筛选ui
            Sui.setVisibility(View.VISIBLE);
        }else if (p==-3){//隐藏多选与筛选ui
            //设置属性为隐藏
            Cui.setVisibility(View.GONE);
            Sui.setVisibility(View.GONE);
//            Call.setVisibility(View.GONE);
//            delete.setVisibility(View.GONE);
//            more.setVisibility(View.GONE);
            Cback_ui.setVisibility(View.GONE);
            Cmore.setVisibility(View.GONE);
            Cpattern=false;
            Cbook=new ArrayList<book>();
            for (book book:books) {
                book.setP(false);
            }
//            //实例化适配器   上下文 ，数据链表;
//            spq = new shu_spq(Activity_Search.this,books);
//            //根据适配器生成书
//            bookLV.setAdapter(spq);
            spq.notifyDataSetChanged();//更新适配器
        }else if (p==-4){
            boolean p1=false;//用于判断是否全为选中状态
            if (Cbook.size()==this.books.size()){
                p1=true;
            }
            Cbook=new LinkedList<book>();
            if (!p1){
                for (int i1=0;i1<this.books.size();i1++){
                    this.books.get(i1).setP(true);
                    Cbook.add(this.books.get(i1));
                }
                Call.setImageResource(R.drawable.choose_1);
            }else {
                for (int i1=0;i1<this.books.size();i1++){
                    this.books.get(i1).setP(false);
                }
                Call.setImageResource(R.drawable.choose);
            }
//            //实例化适配器   上下文 ，数据链表;
//            spq = new shu_spq(Activity_Search.this,books);
//            //根据适配器生成书
//            bookLV.setAdapter(spq);
            spq.notifyDataSetChanged();//更新适配器
            Cshow.setText("已选中"+Cbook.size()+"本");
        }else {//多选点击事件
            Cui.setVisibility(View.VISIBLE);
//            Sui.setVisibility(View.VISIBLE);
//            Call.setVisibility(View.VISIBLE);
//            delete.setVisibility(View.VISIBLE);
            Cback_ui.setVisibility(View.VISIBLE);
            //判断是否在多选模式
            if (!Cpattern){
                Cpattern=true;
            }

            if (books.get(p).getP()){
                books.get(p).setP(false);
                Cbook.remove(cshu);
                if (Cbook.size()==0){
                    update_ui(-3,null);
                }
            }else {
                books.get(p).setP(true);
                Cbook.add(cshu);
            }
            if (Cbook.size()==books.size()){
                Call.setImageResource(R.drawable.choose_1);
            }else {
                Call.setImageResource(R.drawable.choose);
            }
//            //实例化适配器   上下文 ，数据链表;
//            spq = new shu_spq(Activity_Search.this,books);
//            //根据适配器生成书
//            bookLV.setAdapter(spq);
            spq.notifyDataSetChanged();//更新适配器
            Cshow.setText("已选中"+Cbook.size()+"本");

        }
    }
    //初始化加载
    private void initViews(){
        back=(ImageView) findViewById(R.id.search_fh);
        Call=(ImageView) findViewById(R.id.search_4_all);
        delete=(ImageView) findViewById(R.id.search_4_sc);
        more=(ImageView) findViewById(R.id.search_4_more);
        searching=(ImageView) findViewById(R.id.search_2_search);
        search=(EditText) findViewById(R.id.search_2_ET);
        Cback=(TextView) findViewById(R.id.search_2_2_choose_quxiao);
        Cshow=(TextView) findViewById(R.id.search_2_2_choose_xzs);
        screen=(TextView) findViewById(R.id.screen);
        Cmove=(TextView) findViewById(R.id.site_set_move);
        Clike=(TextView) findViewById(R.id.site_set_delete);
        Sback=(TextView) findViewById(R.id.search_5_top_x);
        Sscope_all=(TextView) findViewById(R.id.Sscope_all);
        Sking_all=(TextView) findViewById(R.id.Sking_all);
        Ssite_all=(TextView) findViewById(R.id.Ssite_all);
        Slike=(TextView) findViewById(R.id.like);
        Ssubmit=(TextView) findViewById(R.id.submit);
        CBbook_name=(CheckBox) findViewById(R.id.scope_CB_1);
        CBauthor=(CheckBox) findViewById(R.id.scope_CB_2);
        CBnationality=(CheckBox) findViewById(R.id.scope_CB_3);
        CBcomment=(CheckBox) findViewById(R.id.scope_CB_4);
        CBxs=(CheckBox) findViewById(R.id.king_CB_1);
        CBmz=(CheckBox) findViewById(R.id.king_CB_2);
        CBkh=(CheckBox) findViewById(R.id.king_CB_3);
        CBzs=(CheckBox) findViewById(R.id.king_CB_4);
        CBls=(CheckBox) findViewById(R.id.king_CB_5);
        CBzx=(CheckBox) findViewById(R.id.king_CB_6);
        bookLV=(ListView) findViewById(R.id.search_3);
        siteLV=(ListView) findViewById(R.id.siteLV);
        Sbackground=(RelativeLayout) findViewById(R.id.search_tc);
        Sui=(RelativeLayout) findViewById(R.id.search_5);
        Cui=(RelativeLayout) findViewById(R.id.search_4);
        Cback_ui=(RelativeLayout) findViewById(R.id.search_2_2);
        Cmore=(RelativeLayout) findViewById(R.id.site_more);
        sort=(Spinner) findViewById(R.id.search_2_3_sort_sp);
        //设置属性为隐藏
        Sui.setVisibility(View.GONE);
        Cback_ui.setVisibility(View.GONE);
        Cmore.setVisibility(View.GONE);
        Cui.setVisibility(View.GONE);
//        Call.setVisibility(View.GONE);
//        delete.setVisibility(View.GONE);
//        more.setVisibility(View.GONE);
        //实例化下拉框适配器并写入数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort_S);
        //绑定适配器与下拉框
        sort.setAdapter(adapter);
        sort.setSelection(0);
        sql=new sql(Activity_Search.this);
        //得一个可写的SQLiteDatabase对象
        db= sql.getWritableDatabase();

        sites=sql.cxbj_site(db);
        for (site site:sites) {
            site.setP(true);
        }

        intent=getIntent();//设置intent为接收模式
        int S=(int) intent.getSerializableExtra("S");

        if (S==1){
            keyword=(String) intent.getSerializableExtra("S_ET");
            search.setText(keyword);
        }else if (S==2){
            int k=(int) intent.getSerializableExtra("S_K");
            //1.名著，2.小说，3.科幻，4.知识，5.历史，6.哲学
            if (k==1){
                //                书名   作者 国籍 备注  小说  名著  科幻   知识   历史   哲学  收藏
                Cbx=new Boolean[]{true,true,true,true,false,true,false,false,false,false,false};
            }else if (k==2){
                //                书名   作者 国籍 备注  小说  名著  科幻   知识   历史   哲学  收藏
                Cbx=new Boolean[]{true,true,true,true,true,false,false,false,false,false,false};
            }else if (k==3){
                //                书名   作者 国籍 备注  小说  名著  科幻   知识   历史   哲学  收藏
                Cbx=new Boolean[]{true,true,true,true,false,false,true,false,false,false,false};
            }else if (k==4){
                //                书名   作者 国籍 备注  小说  名著  科幻   知识   历史   哲学  收藏
                Cbx=new Boolean[]{true,true,true,true,false,false,false,true,false,false,false};
            }else if (k==5){
                //                书名   作者 国籍 备注  小说  名著  科幻   知识   历史   哲学  收藏
                Cbx=new Boolean[]{true,true,true,true,false,false,false,false,true,false,false};
            }else if (k==6){
                //                书名   作者 国籍 备注  小说  名著  科幻   知识   历史   哲学  收藏
                Cbx=new Boolean[]{true,true,true,true,false,false,false,false,false,true,false};
            }
        }else if (S==3){
            //             书名   作者 国籍 备注  小说 名著  科幻 知识  历史  哲学  收藏
            Cbx=new Boolean[]{true,true,true,true,true,true,true,true,true,true,true};
        }else if (S==4){
            int id=(int) intent.getSerializableExtra("S_s");
            for (site site:sites) {
                site.setP(false);
            }
            sites.get(id).setP(true);
        }


//             书名   作者 国籍 备注  小说 名著  科幻 知识  历史  哲学  收藏
        CBbook_name.setChecked(Cbx[0]);
        CBauthor.setChecked(Cbx[1]);
        CBnationality.setChecked(Cbx[2]);
        CBcomment.setChecked(Cbx[3]);
        CBxs.setChecked(Cbx[4]);
        CBmz.setChecked(Cbx[5]);
        CBkh.setChecked(Cbx[6]);
        CBzs.setChecked(Cbx[7]);
        CBls.setChecked(Cbx[8]);
        CBzx.setChecked(Cbx[9]);
        if (Cbx[10]){
            Slike.setBackgroundResource(R.drawable.yuan_jiao_blue);
        }else {
            Slike.setBackgroundResource(R.drawable.yuan_jiao_g);
        }
        books=sql.cxbj_intact_s_book(db,keyword,sites,0,Cbx);
        //实例化适配器   上下文 ，数据链表;
        spq = new shu_spq(Activity_Search.this,books);
        //根据适配器生成书
        bookLV.setAdapter(spq);

        //实例化适配器   上下文 ，数据链表;
        spq_s = new site_spq(Activity_Search.this,sites,false);
        //根据适配器生成书
        siteLV.setAdapter(spq_s);
    }
    //每次被位于顶端时调用
    @Override
    public void onResume() {
        initViews();
        super.onResume();
    }
}