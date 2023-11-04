package com.example.my_books;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Activity_Site extends Activity {
    //        取消  重命名
    TextView logout,ren,delete,move;
    RelativeLayout r_more,r_set;
    ImageView back,add,more;
    sql sql;
    SQLiteDatabase db;
    ListView site_s;
    site_spq spq;//书模板的适配器
    List<site> sites;
    //选中模式判断
    boolean Cpattern=false,Cmove=false;
    //选中书架信息
    site Csite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);
        initViews();
        //返回事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //添加事件
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //生成带输入框的对话框
                final EditText inputServer = new EditText(Activity_Site.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Site.this);
                builder.setTitle("请输入书架名");
                builder.setView(inputServer);
                builder.setPositiveButton("确定" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        if (inputName.equals("")) {
                            Toast.makeText(Activity_Site.this, "书架名不能为空", Toast.LENGTH_LONG).show();
                        } else {
                            if (sql.add_site(db,inputName)>0){
                                updateLv(-1);
                                Toast.makeText(Activity_Site.this, inputName+"添加成功", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(Activity_Site.this, inputName+"添加失败", Toast.LENGTH_LONG).show();
                            }
                        }
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
        //书架长按事件
        site_s.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!Cpattern){
                    updateLv(position);
                }
                return false;
            }
        });
        //书架点击事件
        site_s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!Cpattern){
                    Intent intent=new Intent(Activity_Site.this,Activity_Search.class);//设置跳转
                    intent.putExtra("S",4);
                    intent.putExtra("S_s",position);
                    startActivity(intent);//执行跳转
                }else {
                    updateLv(position);
                }
            }
        });
        //取消事件
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLv(-2);
            }
        });
        //更多事件
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cmove){
                    more.setImageResource(R.drawable.more_1);
                    r_more.setVisibility(View.VISIBLE);
                    Cmove=true;
                }else {
                    more.setImageResource(R.drawable.more);
                    r_more.setVisibility(View.GONE);
                    Cmove=false;
                }
            }
        });
        //重命名事件
        ren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //生成带输入框的对话框
                final EditText inputServer = new EditText(Activity_Site.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Site.this);
                builder.setTitle("请输入新书架名");
                builder.setView(inputServer);
                builder.setPositiveButton("确定" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        if (inputName.equals("")) {
                            Toast.makeText(Activity_Site.this, "书架名不能为空", Toast.LENGTH_LONG).show();
                        } else {
                            Csite.setSite(inputName);
                            if (sql.update_site(db,Csite)>0){
                                updateLv(-1);
                                Toast.makeText(Activity_Site.this, inputName+"修改成功", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(Activity_Site.this, inputName+"修改失败", Toast.LENGTH_LONG).show();
                            }
                        }
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
        //删除事件
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog aldg1;//声明一个对话框
                AlertDialog.Builder adBd1=new AlertDialog.Builder(Activity_Site.this);//实例化一个对话框格式
                //设置富文本
                SpannableString spannableString = new SpannableString("确定删除"+Csite.getSite()+"吗？(该书架存放的书将一并删除)");
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
                spannableString.setSpan(colorSpan, spannableString.length()-13,spannableString.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                adBd1.setMessage(spannableString);//给对话框格式设置文本
                //给对话框格式设置按钮并设置其点击方法
                adBd1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sql.sc_site(db,Csite)>-1){
                            Toast.makeText(Activity_Site.this, Csite.getSite()+"删除成功", Toast.LENGTH_LONG).show();
                            updateLv(-1);
                        }else {
                            Toast.makeText(Activity_Site.this, Csite.getSite()+"删除失败", Toast.LENGTH_LONG).show();
                        }
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
        //移动事件
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] sj=new String[sites.size()-1];
                int i=0;
                for (site site:sites) {
                    if (!site.getP()){
                        sj[i]=site.getSite();
                        i++;
                    }
                }
                Spinner inputServer=new Spinner(Activity_Site.this);;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Activity_Site.this,android.R.layout.simple_spinner_item,sj);
                inputServer.setAdapter(adapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Site.this);
                builder.setTitle("请选择移动至书架");
                builder.setView(inputServer);
                builder.setPositiveButton("确定" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int j=(int) inputServer.getSelectedItemId();
                        for (int i = 0; i < sites.size(); i++) {
                            if (sites.get(i).getId()!=Csite.getId()){
                                if (j==0){
                                    j=sites.get(i).getId();
                                    break;
                                }
                                j--;
                            }

                        }
                        List<book> bookList=sql.cxsite_book(db,Csite.getId());
                        for (book b:bookList) {
                            b.setSite(j);
                        }
                        int a=(int) sql.update_book(db,bookList);
                        Toast.makeText(Activity_Site.this, "已将"+a+"本书籍移动", Toast.LENGTH_LONG).show();
                        updateLv(-3);
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
    }
    private void initViews(){
        site_s=(ListView) findViewById(R.id.site_lv);
        logout=(TextView) findViewById(R.id.site_set_qx);
        ren=(TextView) findViewById(R.id.site_set_ren);
        delete=(TextView) findViewById(R.id.site_set_delete);
        move=(TextView) findViewById(R.id.site_set_move);
        r_more=(RelativeLayout) findViewById(R.id.site_more);
        r_set=(RelativeLayout) findViewById(R.id.site_set);
        back=(ImageView) findViewById(R.id.site_fh);
        add=(ImageView) findViewById(R.id.site_add);
        more=(ImageView) findViewById(R.id.site_site_more);
        r_more.setVisibility(View.GONE);
        r_set.setVisibility(View.GONE);

        sql=new sql(this);
        db = sql.getReadableDatabase();//得到的是SQLiteDatabase对象
        sites=sql.cxbj_site_quantity(db);
        //实例化适配器   上下文 ，数据链表;
        spq = new site_spq(this,sites);
        //根据适配器生成书
        site_s.setAdapter(spq);
    }

    private void updateLv(int i){
        if (i==-1){
            sites=sql.cxbj_site_quantity(db);
            Csite=new site();
            r_more.setVisibility(View.GONE);
            r_set.setVisibility(View.GONE);
            Cpattern=false;
        }else if (i==-2){
            Cpattern=false;
            for (site site:sites) {
                site.setP(false);
            }
            r_more.setVisibility(View.GONE);
            r_set.setVisibility(View.GONE);
        }else if (i==-3){
            sites=sql.cxbj_site_quantity(db);
            for (site s:sites) {
                if (s.getId()==Csite.getId()){
                    s.setP(true);
                }
            }
        }else {
            Cpattern=true;
            for (site site:sites) {
                site.setP(false);
            }
            if (!Cmove){
                r_set.setVisibility(View.VISIBLE);
                r_more.setVisibility(View.GONE);
                more.setImageResource(R.drawable.more);
            }else {
                r_set.setVisibility(View.VISIBLE);
                r_more.setVisibility(View.VISIBLE);
                more.setImageResource(R.drawable.more_1);
            }
            sites.get(i).setP(true);
            Csite=sites.get(i);
        }
        //实例化适配器   上下文 ，数据链表;
        spq = new site_spq(this,sites);
        //根据适配器生成书
        site_s.setAdapter(spq);
        spq.notifyDataSetChanged();//更新适配器
    }
}