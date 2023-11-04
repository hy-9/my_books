package com.example.my_books;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link myFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myFragment extends Fragment {
    RelativeLayout register,user,function,logout,like,my_site;
    TextView site,user_name,user_id,book_amount,site_amount;
    sql sql;
    SQLiteDatabase db;
    ListView site_s;
    site_spq spq;//书模板的适配器
    List<site> sites;
    user u;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public myFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mian.
     */
    // TODO: Rename and change types and number of parameters
    public static myFragment newInstance(String param1, String param2) {
        myFragment fragment = new myFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated=true;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_my, container, false);

        initViews(view);//初始化控件
        initViews();

        //登录
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Activity_register.class);
                startActivity(intent);
            }
        });
        //退出登录
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog aldg1;//声明一个对话框
                AlertDialog.Builder adBd1=new AlertDialog.Builder(getContext());//实例化一个对话框格式
                adBd1.setMessage("确定退出当前登录吗？");//给对话框格式设置文本
                //给对话框格式设置按钮并设置其点击方法
                adBd1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sql.scbj(db);
                        Toast.makeText(getContext(), "退出登录成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(),Activity_register.class);
                        startActivity(intent);
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
        //书架事件
        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Activity_Site.class);
                startActivity(intent);
            }
        });
        //收藏事件
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",3);
                startActivity(intent);//执行跳转
            }
        });
        //书架详情
        site_s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",4);
                intent.putExtra("S_s",position);
                startActivity(intent);//执行跳转
            }
        });
        // Inflate the layout for this fragment
        return view;

    }
    //初始化控件
    private void initViews(){
        sql=new sql(getContext());
        db = sql.getReadableDatabase();//得到的是SQLiteDatabase对象
        if (sql.cxbj(db).getId()==0){
            //设置可见属性为显示
            register.setVisibility(View.VISIBLE);
            //设置属性为隐藏
            user.setVisibility(View.GONE);
            site.setVisibility(View.GONE);
            function.setVisibility(View.GONE);
            my_site.setVisibility(View.GONE);
        }else {
            register.setVisibility(View.GONE);
            user.setVisibility(View.VISIBLE);
            site.setVisibility(View.VISIBLE);
            function.setVisibility(View.VISIBLE);
            my_site.setVisibility(View.VISIBLE);
        }
        sites=sql.cxbj_site_quantity(db);
        //实例化适配器   上下文 ，数据链表;
        spq = new site_spq(getContext(),sites);
        //根据适配器生成书
        site_s.setAdapter(spq);
        int[] a=sql.cxbj_book_site_amount(db);
        u= sql.cxbj(db);
        user_name.setText(u.getUser_name());
        user_id.setText("用户id:"+u.getId());
        site_amount.setText("书架数"+a[1]+"个");
        book_amount.setText("总藏书"+a[0]+"本");
    }
    private void initViews(View view){

        register=view.findViewById(R.id.my_register);
        user=view.findViewById(R.id.my_dingdan);
        site=view.findViewById(R.id.my_site_text);
        function=view.findViewById(R.id.my_else);
        logout=view.findViewById(R.id.my_logout);
        site_s=view.findViewById(R.id.my_site_lv);
        user_name=view.findViewById(R.id.my_xq_yhm);
        user_id=view.findViewById(R.id.my_id);
        site_amount=view.findViewById(R.id.my_xq_site);
        book_amount=view.findViewById(R.id.my_xq_books);
        like=view.findViewById(R.id.my_like);
        my_site=view.findViewById(R.id.my_site);
    }
    //每次被位于顶端时调用
    @Override
    public void onResume() {
        initViews();
        super.onResume();
    }
    private boolean isCreated=false;
    //碎片移动时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated){
            return;
        }else {
        }
    }
}