package com.example.my_books.activity.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.my_books.R;
import com.example.my_books.data.Repository;
import com.example.my_books.data.database.AppDatabase;
import com.example.my_books.data.entity.User;
import com.example.my_books.model.user;
import com.example.my_books.sql.sql;

import java.util.ArrayList;
import java.util.List;

public class Activity_register extends AppCompatActivity {
    ImageView backButton, showPasswordButton;
    EditText inputUserName, inputUserPassword;
    TextView dl,yhxy,zhu_che,biao;
    RadioButton TermsCheckbox;
    boolean isRegister =false;
    boolean p2=false;


    //单独线程动态更新页面    1.更新状态   2.看密码
    private Handler handler=new Handler(){
        //更新方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int id=msg.what;
            if (id==1){
                if (!isRegister){
                    biao.setText("注册新用户");
                    dl.setText("注册并登录");
                    zhu_che.setText("登录");
                    isRegister =!isRegister;
                }else {
                    biao.setText("账号密码登录");
                    dl.setText("登录");
                    zhu_che.setText("注册成为新用户");
                    isRegister =!isRegister;
                }
                inputUserName.setText(null);
                inputUserPassword.setText(null);
            }else if (id==2){
                if(!p2){
                    showPasswordButton.setImageDrawable(getResources().getDrawable(R.drawable.zhen_yan));
                    inputUserPassword.setInputType(InputType.TYPE_NULL);
                    p2=!p2;
                }else {
                    showPasswordButton.setImageDrawable(getResources().getDrawable(R.drawable.bi_yan));
                    inputUserPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    p2=!p2;
                }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //绑定控件与变量
        bang_ding();
        sql initSql =new sql(Activity_register.this);//数据库类
        SQLiteDatabase db = initSql.getReadableDatabase();//得到的是SQLiteDatabase对象
        AppDatabase appDatabase = AppDatabase.Companion.getInstance(this);
        Repository repository = new Repository(appDatabase.userDao());
        RegisterViewModel registerViewModel = new RegisterViewModel(repository);
        List<User> users = new ArrayList<User>();
        registerViewModel.getAllUserLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> value) {
                users.addAll(value);
                    List<user> allUsers = initSql.queryAllUsersByUserName(db, inputUserName.getText()+"");
                    if (users.size() != 0 && users.get(0).getPassword().equals(inputUserPassword.getText()+"")){
                        Toast.makeText(getApplicationContext(), "用户"+users.get(0).getUser_name()+"登录成功", Toast.LENGTH_LONG).show();
                        initSql.gxbj(db,allUsers.get(0));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                    }
                    registerViewModel.clearAllUsersState();
            }
        });
        //返回事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//返回上一级
            }
        });
        //登录事件
        dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TermsCheckbox.isChecked()) {
                    if (inputUserName.getText() + "" != "" && inputUserPassword.getText() + "" != "") {
                        //判断为登录还是注册界面
                        if (!isRegister){//登录界面
                            registerViewModel.queryAllUsers(inputUserName.getText() + "");
                            registerViewModel.getAllUser();
//                            List<user> allUsers = initSql.queryAllUsersByUserName(db, inputUserName.getText()+"");
//                            if (allUsers.size() != 0 && allUsers.get(0).getPassword().equals(inputUserPassword.getText()+"")){
//                            if (users.size() != 0 && users.get(0).getPassword().equals(inputUserPassword.getText()+"")){
//                                Toast.makeText(getApplicationContext(), "用户"+users.get(0).getUser_name()+"登录成功", Toast.LENGTH_LONG).show();
//                                initSql.gxbj(db,allUsers.get(0));
//                                finish();
//                            }else {
//                                Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();
//                            }
                        } else {//注册界面
                            List<user> yh= initSql.queryAllUsersByUserName(db, inputUserName.getText()+"");
                            if (yh.size()==0){
                                user user1 = new user(inputUserName.getText()+"", inputUserPassword.getText()+"");
                                user1.setId((int) initSql.zcyh(db,user1));
                                 if (user1.getId()!=0){
                                     Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                                        initSql.gxbj(db,user1);
                                        finish();
                                 }else {
                                     Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_LONG).show();
                                     finish();
                                 }
                            }else {
                                Toast.makeText(getApplicationContext(), "用户名以注册", Toast.LENGTH_LONG).show();
                            }


                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请同意用户协议", Toast.LENGTH_LONG).show();
                }
            }
        });
        //用户协议事件
        yhxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog aldg1;//声明一个对话框
                AlertDialog.Builder adBd1 = new AlertDialog.Builder(Activity_register.this);//实例化一个对话框格式
                adBd1.setMessage("此软件仅供学习交流参考");//给对话框格式设置文本
                //给对话框格式设置按钮并设置其点击方法
                adBd1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                aldg1 = adBd1.create();//将对话框格式绑定到对话框
                aldg1.show();//生成对话框
            }
        });
        //注册事件
        zhu_che.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //状态转换
                Message message = handler.obtainMessage();//新建通信变量
                message.what = 1;//what属性
                handler.sendMessage(message);//通信发送信息
            }
        });
        //看密码
        showPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = handler.obtainMessage();//新建通信变量
                message.what = 2;//what属性
                handler.sendMessage(message);//通信发送信息
            }
        });
    }

    //绑定控件与变量
    public void bang_ding() {
        backButton = (ImageView) findViewById(R.id.register_fan_huei);
        showPasswordButton = (ImageView) findViewById(R.id.register_kmm);
        inputUserName = (EditText) findViewById(R.id.register_yhm);
        inputUserPassword = (EditText) findViewById(R.id.register_mm);
        dl = (TextView) findViewById(R.id.register_dl);
        biao = (TextView) findViewById(R.id.biao);
        yhxy = (TextView) findViewById(R.id.register_2);
        zhu_che = (TextView) findViewById(R.id.register_zhu_chei);
        TermsCheckbox = (RadioButton) findViewById(R.id.register_3);
    }
}