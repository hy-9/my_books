package com.example.my_books;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    RelativeLayout register,details,to_2,choose,choose_tips,Zhu_searching;
                                    //1.名著，2.小说，3.科幻，4.知识，5.历史，6.哲学
    TextView CT_cancel,CT_amount,AB_amount,mz_amount,xs_amount,kh_amount,zs_amount,
            ls_amount,zx_amount,xs,mz,kh,zs,ls,zx;
    ListView books;
    ImageView add_book,to_1,Call,delete,searching;
    sql sql;
    SQLiteDatabase db ;
    shu_spq spq;//书模板的适配器
    //选中模式判断
    boolean Cpattern=false;
    //所有书籍列表
    List<book> book=new LinkedList<book>();
    //选中书籍列表
    List<book> Cbook= new LinkedList<book>();
    //所有地址列表
    List<site> sites=new LinkedList<site>();
    EditText editText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
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
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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
        View view =inflater.inflate(R.layout.fragment_home, container, false);


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
        //书的点击事件会跳转到书的详细界面
        books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                book cshu=(book) adapterView.getItemAtPosition(i);//把点击的那条数据包装到cshu里
                if (!Cpattern){
                    Intent intent=new Intent(getContext(),Activity_Book_Particulars.class);//设置跳转
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
                }else{
                    updateLv(i,cshu);
                }
            }
        });
        //添加书籍事件
        add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Book_Particulars.class);//设置跳转
                intent.putExtra("pattern",0);
                startActivity(intent);//执行跳转
            }
        });
        //书的长按事件
        books.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Cpattern){
                    pattern_change();
                    book cshu=(book)adapterView.getItemAtPosition(i);//把点击的那条数据包装到cshu里
                    updateLv(i,cshu);//更改背景方法
                }
                return false;
            }
        });
        //退出多选状态
        CT_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLv(-1,null);
            }
        });
        //多选状态全选事件
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLv(-2,null);
            }
        });
        //删除事件
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cbook.size()==0){
                    Toast.makeText(getContext(),"请选择要删除的书",Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog aldg1;//声明一个对话框
                    AlertDialog.Builder adBd1=new AlertDialog.Builder(getContext());//实例化一个对话框格式
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
                            updateLv(-3,null);
                            if (p1){
                                Toast.makeText(getContext(),"以删除选中的"+a+"本书",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(),"删除失败 未知错误",Toast.LENGTH_SHORT).show();
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
            }
        });
        //检索事件
        searching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                if (editText.getText().toString().equals("")){
                    intent.putExtra("S",0);
                }else{
                    intent.putExtra("S",1);
                    intent.putExtra("S_ET",editText.getText().toString());
                }
                startActivity(intent);//执行跳转
            }
        });
        ////1.名著，2.小说，3.科幻，4.知识，5.历史，6.哲学
        //名著点击事件
        mz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",1);
                startActivity(intent);//执行跳转
            }
        });
        mz_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",1);
                startActivity(intent);//执行跳转
            }
        });
        //小说点击事件
        xs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",2);
                startActivity(intent);//执行跳转
            }
        });
        xs_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",2);
                startActivity(intent);//执行跳转
            }
        });
        //科幻点击事件
        kh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",3);
                startActivity(intent);//执行跳转
            }
        });
        kh_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",3);
                startActivity(intent);//执行跳转
            }
        });
        //知识点击事件
        zs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",4);
                startActivity(intent);//执行跳转
            }
        });
        zs_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",4);
                startActivity(intent);//执行跳转
            }
        });
        //历史点击事件
        ls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",5);
                startActivity(intent);//执行跳转
            }
        });
        ls_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",5);
                startActivity(intent);//执行跳转
            }
        });
        //哲学点击事件
        zx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",6);
                startActivity(intent);//执行跳转
            }
        });
        zx_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Activity_Search.class);//设置跳转
                intent.putExtra("S",2);
                intent.putExtra("S_K",6);
                startActivity(intent);//执行跳转
            }
        });
        // Inflate the layout for this fragment
        return view;

    }
    //多选模式变更
    private void pattern_change(){
        if (Cpattern){
            choose_tips.setVisibility(View.GONE);
            choose.setVisibility(View.GONE);
        }else{
            choose_tips.setVisibility(View.VISIBLE);
            choose.setVisibility(View.VISIBLE);
        }Cpattern=!Cpattern;
    }
    //更新多选模式下的lv
    private void updateLv(int i,book book){
        if (i==-1){//取消事件
            for (book b:this.book) {
                b.setP(false);
            }
            Cbook=new LinkedList<book>();
        }else if (i==-2){//全选事件
            boolean p1=false;//用于判断是否全为选中状态
            if (Cbook.size()==this.book.size()){
                p1=true;
            }

            Cbook=new LinkedList<book>();
            if (!p1){
                for (int i1=0;i1<this.book.size();i1++){
                    this.book.get(i1).setP(true);
                    Cbook.add(this.book.get(i1));
                }
                Call.setImageResource(R.drawable.choose_1);
            }else {
                for (int i1=0;i1<this.book.size();i1++){
                    this.book.get(i1).setP(false);
                }
                Call.setImageResource(R.drawable.choose);
            }
        }else if (i==-3){//删除事件
            this.book=sql.cxbj_intact_book(db,sites);
            Cbook=new LinkedList<book>();
            //实例化适配器   上下文 ，数据链表;
            spq = new shu_spq(getContext(),this.book);
            //根据适配器生成书
            books.setAdapter(spq);
        }else if (!this.book.get(i).getP()){
            book.setP(true);//更改其背景颜色的变量
            this.book.set(i,book);//更新数据链表中的数据
            Cbook.add(book);//把书添加多选数据
            if (Cbook.size()==this.book.size()){
                Call.setImageResource(R.drawable.choose_1);
            }else {
                Call.setImageResource(R.drawable.choose);
            }
        }else{
            Cbook.remove(book);//清空多选数据
            book.setP(false);//更改其背景颜色的变量
            this.book.set(i,book);//更新数据链表中的数据
            if (Cbook.size()==this.book.size()){
                Call.setImageResource(R.drawable.choose_1);
            }else {
                Call.setImageResource(R.drawable.choose);
            }
        }
        if (Cbook.size()==0&&i!=-2){//选中为0退出多选
            pattern_change();
        }
        CT_amount.setText("已选中"+Cbook.size()+"本");
        spq.notifyDataSetChanged();//更新适配器
    }
    //初始化加载
    private void initViews() {
        //重置多选模式
        Cpattern=false;
        Cbook= new LinkedList<book>();

        sql=new sql(getContext());
        db = sql.getReadableDatabase();//得到的是SQLiteDatabase对象
        //设置可见
        if (sql.cxbj(db).getId()==0){
            //设置可见属性为显示
            register.setVisibility(View.VISIBLE);
            //设置属性为隐藏
            to_1.setVisibility(View.GONE);
            details.setVisibility(View.GONE);
            to_2.setVisibility(View.GONE);
            add_book.setVisibility(View.GONE);
            books.setVisibility(View.GONE);
            Zhu_searching.setVisibility(View.GONE);

        }else {
            register.setVisibility(View.GONE);
            to_1.setVisibility(View.VISIBLE);
            details.setVisibility(View.VISIBLE);
            to_2.setVisibility(View.VISIBLE);
            add_book.setVisibility(View.VISIBLE);
            books.setVisibility(View.VISIBLE);
            Zhu_searching.setVisibility(View.VISIBLE);
        }
        sites=sql.cxbj_site(db);
        book=sql.cxbj_intact_book(db,sites);
        //实例化适配器   上下文 ，数据链表;
        spq = new shu_spq(getContext(),book);
        //根据适配器生成书
        books.setAdapter(spq);
        //填充数量
        int[] AllKing=sql.cxbj_book_AllKing_amount(db);
        AB_amount.setText(String.valueOf(book.size()));
        //1.名著，2.小说，3.科幻，4.知识，5.历史，6.哲学
        mz_amount.setText(String.valueOf(AllKing[0]));
        xs_amount.setText(String.valueOf(AllKing[1]));
        kh_amount.setText(String.valueOf(AllKing[2]));
        zs_amount.setText(String.valueOf(AllKing[3]));
        ls_amount.setText(String.valueOf(AllKing[4]));
        zx_amount.setText(String.valueOf(AllKing[5]));
    }
    //初始化加载
    private void initViews(View view) {
        register=view.findViewById(R.id.home_register);
        to_1=view.findViewById(R.id.Zhu_tofgx);
        details=view.findViewById(R.id.Zhu_xq);
        to_2=view.findViewById(R.id.Zhu_xqfgx);
        choose=view.findViewById(R.id.Zhu_choose);
        add_book=view.findViewById(R.id.Zhu_tj);
        books=view.findViewById(R.id.Zhu_shu);
        delete=view.findViewById(R.id.Zhu_choose_sc);
        choose_tips=view.findViewById(R.id.Zhu_choose_tips);
        CT_cancel=view.findViewById(R.id.Zhu_choose_tips_quxiao);
        CT_amount=view.findViewById(R.id.Zhu_choose_tips_xzs);
        Call=view.findViewById(R.id.Zhu_choose_all);
        AB_amount=view.findViewById(R.id.Zhu_zhongshuliang_s);
        mz_amount=view.findViewById(R.id.Zhu_mz_s);
        xs_amount=view.findViewById(R.id.Zhu_xs_s);
        kh_amount=view.findViewById(R.id.Zhu_kh_s);
        zs_amount=view.findViewById(R.id.Zhu_zs_s);
        ls_amount=view.findViewById(R.id.Zhu_ls_s);
        zx_amount=view.findViewById(R.id.Zhu_zx_s);
        searching=view.findViewById(R.id.Zhu_searching_im);
        editText=view.findViewById(R.id.zhu_ed);
        xs=view.findViewById(R.id.Zhu_xs);
        mz=view.findViewById(R.id.Zhu_mz);
        kh=view.findViewById(R.id.Zhu_kh);
        zs=view.findViewById(R.id.Zhu_zs);
        ls=view.findViewById(R.id.Zhu_ls);
        zx=view.findViewById(R.id.Zhu_zx);
        Zhu_searching =view.findViewById(R.id.Zhu_searching);

        choose_tips.setVisibility(View.GONE);
        choose.setVisibility(View.GONE);
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