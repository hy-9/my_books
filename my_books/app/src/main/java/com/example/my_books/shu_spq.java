package com.example.my_books;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_books.book;

import java.util.LinkedList;
import java.util.List;
//书模板的适配器类
public class shu_spq extends BaseAdapter {
    private List<book> L_s;//shu类型的数据链表
    private Context context;//上下文
    //该类的构造方法要传入上下文和shu类型的数据链表
    public shu_spq(Context context,List<book> Ls){
        //让传入的上下文和数据链表写入该类的变量中
        this.context=context;
        this.L_s=Ls;
    }
    @Override//返回该适配器链表的总的数据数
    public int getCount() {
        return L_s.size();
    }
    @Override//根据第i个来返回L_s中的第i条数据
    public Object getItem(int i) {
        return L_s.get(i);
    }
    @Override//根据第i个来返回其id
    public long getItemId(int i) {
        return i;
    }
    //    @Override//写入并返回ListView
    public View getView(int i, View view, ViewGroup viewGroup) {
        dsccq ccq;//短时存储器
        View view1;//一个中转的view用于给短时存储器提供数据
        //判断view是否为空若不为空就使用view1来短时存储器中的id赋值来节省资源
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.book,viewGroup,false);//创造一个条目
            ccq=new dsccq();//实例化存储器
            //绑定变量与控件
            ccq.sm=(TextView) view.findViewById(R.id.mb_sm);
            ccq.zz=(TextView) view.findViewById(R.id.mb_zz);
            ccq.gj=(TextView) view.findViewById(R.id.mb_gj);
            ccq.lb=(TextView) view.findViewById(R.id.mb_lb);
            ccq.bz=(TextView) view.findViewById(R.id.mb_bz);
            ccq.cfd=(TextView) view.findViewById(R.id.mb_cfd);
            ccq.date=(TextView) view.findViewById(R.id.mb_date);
            ccq.tb=(ImageView) view.findViewById(R.id.mb_tb);
            ccq.like=(ImageView) view.findViewById(R.id.book_like);
            ccq.bj=(ImageView) view.findViewById(R.id.book_background);
//            ccq.dx=(CheckBox) view.findViewById(R.id.mb_dx);
            view.setTag(ccq);//将短时存储器中的变量设置给view
        }else{
            view1=view;//把view中的信息赋予view1
            ccq=(dsccq)view1.getTag();//将view1中的id数据赋值给短时存储器
        }
        //将shu中的数据赋值给短时存储器中储存的控件
//        ccq.dx.setChecked(L_s.get(i).getP());
        ccq.sm.setText(L_s.get(i).getBook_name());
        ccq.zz.setText(L_s.get(i).getAuthor());
        ccq.gj.setText(L_s.get(i).getNationality());
        ccq.bz.setText(L_s.get(i).getComment());
        ccq.cfd.setText(L_s.get(i).getSite_name());
        ccq.date.setText(L_s.get(i).getDate());
        //判断是否被多选中，选中变色
        if (L_s.get(i).getP()){
            ccq.bj.setBackgroundColor(Color.parseColor("#66ADCD"));
        }else {
            ccq.bj.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        if (L_s.get(i).getLike()==1){
            ccq.like.setVisibility(View.VISIBLE);
        }else {
            ccq.like.setVisibility(View.GONE);
        }
        //判断书的种类来设置图标与文字
        switch (L_s.get(i).getKing()){//zl:1.名著，2.小说，3.科幻，4.知识，5.历史，6.哲学
            case 1:
                ccq.tb.setImageResource(R.drawable.mz);
                ccq.lb.setText("名著");
                break;
            case 2:
                ccq.tb.setImageResource(R.drawable.xs);
                ccq.lb.setText("小说");
                break;
            case 3:
                ccq.tb.setImageResource(R.drawable.kh);
                ccq.lb.setText("科幻");
                break;
            case 4:
                ccq.tb.setImageResource(R.drawable.zs);
                ccq.lb.setText("知识");
                break;
            case 5:
                ccq.tb.setImageResource(R.drawable.ls);
                ccq.lb.setText("历史");
                break;
            case 6:
                ccq.tb.setImageResource(R.drawable.zx);
                ccq.lb.setText("哲学");
                break;
        }
        return view;//返回创造的条目
    }
}
// 短时存储器用来暂时存放控件的id
class dsccq{
    //              书名  ，   作者  ，   国籍  ，   备注  ， 种类  ，存放地
    TextView sm;
    TextView zz;
    TextView gj;
    TextView lb;
    TextView bz;
    TextView cfd;
    ImageView tb;
    ImageView bj;
    CheckBox dx;
    TextView date;
    ImageView like;
}
