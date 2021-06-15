package com.example.todayhistory.bean;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.todayhistory.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private List<HistoryBean.ResultBean> datas;

    public HistoryAdapter(Context context, List<HistoryBean.ResultBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main_timeline, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HistoryBean.ResultBean resultBean = datas.get(position);
        //判断当前位置年份和上一个位置年份是否相同
        if (position != 0) {
            HistoryBean.ResultBean lastBean = datas.get(position-1);
            if(resultBean.getYear()==lastBean.getYear()){
                holder.timeLayout.setVisibility(View.GONE);
            }else{
                holder.timeLayout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.timeLayout.setVisibility(View.VISIBLE);
        }

        holder.tvTime.setText(resultBean.getYear()+"-"+resultBean.getMonth()+"-"+resultBean.getDay());
        holder.tvTitle.setText(resultBean.getTitle());
        String picURL = resultBean.getPic();
        if(TextUtils.isEmpty(picURL)){
            holder.imgPic.setVisibility(View.GONE);
        }else{
            holder.imgPic.setVisibility(View.VISIBLE);
            Picasso.with(context).load(picURL).into(holder.imgPic);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvTime, tvTitle;
        ImageView imgPic;
        LinearLayout timeLayout;

        public ViewHolder(View v) {
            tvTime = v.findViewById(R.id.item_main_tvTime);
            tvTitle = v.findViewById(R.id.item_main_title);
            imgPic = v.findViewById(R.id.item_main_pic);
            timeLayout = v.findViewById(R.id.item_main_ll);
        }
    }
}
