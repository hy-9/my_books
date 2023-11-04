package com.example.my_books;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;
    //三个Tab对应的布局
    private LinearLayout mTab1,mTab2,mTab3;
    //三个Tab对应的ImageButton
    private ImageButton mImg1,mImg2,mImg3;
    private Intent intent;
    private RelativeLayout dl;
    sql sql =new sql(MainActivity.this);
    private boolean yhdl=false;
    private user user;
    private SQLiteDatabase db ;//得到的是SQLiteDatabase对象;
    private TextView my_yhm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initViews();//初始化控件
        selectTab(0);//初始页面为主页面
        initDatas();//填充界面
        initEvents();//初始化事件

        //登录点击事件
        dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!yhdl){
                    Intent intent = new Intent(MainActivity.this,Activity_register.class);
                    startActivity(intent);
                }
            }
        });

    }
    //初始化控件
    private void initViews(){
        mViewPager = (ViewPager) findViewById(R.id.mian_viewpager);

        mTab1 = (LinearLayout) findViewById(R.id.id_tab1);
        mTab2 = (LinearLayout) findViewById(R.id.id_tab2);

        mImg1 = (ImageButton) findViewById(R.id.id_tab_img1);
        mImg2 = (ImageButton) findViewById(R.id.id_tab_img2);

        dl=(RelativeLayout) findViewById(R.id.my_order);
        my_yhm=(TextView) findViewById(R.id.my_yhm);
    }

    private void initEvents(){
        //设置三个Tab的点击事件
        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);

        db= sql.getReadableDatabase();//得到的是SQLiteDatabase对象;
        //        查询本机
        user=sql.cxbj(db);
        if (user.getId() != 0){
            yhdl=true;
            my_yhm.setText(user.getUser_name()+"");
        }else{
            my_yhm.setText("点击登录");
            yhdl=false;
        }
    }

    private void initDatas(){
        mFragments=new ArrayList<>();
        //将三个Fragment加入集合中
        mFragments.add(new homeFragment());
        mFragments.add(new myFragment());

        //初始化适配器
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        //设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //将ViewPager的预加载个数设置为你的所有Fragment的数量,解决滑动时候轮播图出现空白的问题
        mViewPager.setOffscreenPageLimit(mFragments.size());
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面滚动事件
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }
            //页面滚动状态改变事件
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        //先将三个ImageButton置为灰色
        resetImgs();
        switch (view.getId()){
            case R.id.id_tab1:
                selectTab(0);
                break;
            case R.id.id_tab2:
                selectTab(1);
                break;
        }
    }
    //将三个ImageButton设置为灰色
    private void resetImgs() {
        mImg1.setImageResource(R.drawable.home);
        mImg2.setImageResource(R.drawable.my);
    }

    public void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为选中的颜色
        switch (i) {
            case 0:
                mImg1.setImageResource(R.drawable.home_pitch);
                break;
            case 1:
                mImg2.setImageResource(R.drawable.my_pitch);
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }
    public void onResume() {
        super.onResume();
        initEvents();
    }
}
