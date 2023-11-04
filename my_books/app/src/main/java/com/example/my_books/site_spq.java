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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_books.book;

import java.util.LinkedList;
import java.util.List;
//书模板的适配器类
public class site_spq extends BaseAdapter {
    private List<site> L_s;//shu类型的数据链表
    private Context context;//上下文
    private boolean p=true;
    //该类的构造方法要传入上下文和shu类型的数据链表
    public site_spq(Context context,List<site> Ls){
        //让传入的上下文和数据链表写入该类的变量中
        this.context=context;
        this.L_s=Ls;
    }
    public site_spq(Context context,List<site> Ls,boolean p){
        //让传入的上下文和数据链表写入该类的变量中
        this.context=context;
        this.L_s=Ls;
        this.p=p;
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
        site_ccq ccq;//短时存储器
        View view1;//一个中转的view用于给短时存储器提供数据
        //判断view是否为空若不为空就使用view1来短时存储器中的id赋值来节省资源
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.site,viewGroup,false);//创造一个条目
            ccq=new site_ccq();//实例化存储器
            //绑定变量与控件
            ccq.site_name=(TextView) view.findViewById(R.id.site_name);
            ccq.quantity=(TextView) view.findViewById(R.id.quantity);
            ccq.bj=(RelativeLayout) view.findViewById(R.id.site_bj);
            ccq.aa=(TextView) view.findViewById(R.id.aa);
            view.setTag(ccq);//将短时存储器中的变量设置给view
        }else{
            view1=view;//把view中的信息赋予view1
            ccq=(site_ccq) view1.getTag();//将view1中的id数据赋值给短时存储器
        }
        //判断是否被多选中，选中变色
        if (L_s.get(i).getP()){
            ccq.bj.setBackgroundColor(Color.parseColor("#66ADCD"));
        }else {
            ccq.bj.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        //将shu中的数据赋值给短时存储器中储存的控件
        ccq.site_name.setText(L_s.get(i).getSite());
        ccq.quantity.setText(L_s.get(i).getUser_name()+"本");
        if (!p){
            ccq.aa.setVisibility(View.GONE);
            ccq.quantity.setVisibility(View.GONE);
        }
        return view;//返回创造的条目
    }
}
// 短时存储器用来暂时存放控件的id
class site_ccq{
    TextView site_name,quantity,aa;
    RelativeLayout bj;
}
