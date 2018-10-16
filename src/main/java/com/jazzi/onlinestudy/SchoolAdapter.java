package com.jazzi.onlinestudy;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.ViewHolder> {
    private List<School> schools;



    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView schoolImage;
        TextView schoolName;

        public ViewHolder(View view){
            super(view);
            schoolImage=(ImageView) view.findViewById(R.id.school_image);
            schoolName=(TextView) view.findViewById(R.id.school_name);
        }
    }

    public SchoolAdapter(List<School> schoolList){
        schools=schoolList;
    }
    //用于创建ViewHolder实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //获得主布局对象
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.school_item,parent,false);
        //只有通过主布局对象才能获得子布局对象
        /*通过context构造一个LayoutInflater实例,然后调用inflate加载布局文件*/
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    /*用于对子项数据进行赋值
     * 每个子项被滚动到屏幕内的时候执行
     * 通过position与schools得到当前school实例
     * 然后将数据设置到ViewHolder的ImageView和TextView中*/
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        School school =schools.get(position);
        holder.schoolImage.setImageResource(school.getImageId());
        holder.schoolName.setText(school.getName());
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }
}
