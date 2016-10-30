package com.maker.use.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.domain.Comment;

import java.util.ArrayList;

/**
 *  评论列表适配器
 * Created by jzh on 2015/9/28.
 */
public class CommentListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Comment> news;

    public CommentListViewAdapter(Context context, ArrayList<Comment> news) {
        this.context = context;
        this.news = news;
    }


    public void setList(ArrayList<Comment> list) {
        news = list;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_comment, null);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.civ_user_head);
            holder.tv_name = (TextView) convertView.findViewById(R.id.listview_pinglun_item_textView_name);
            holder.tv_location = (TextView) convertView.findViewById(R.id.listview_pinglun_item_textView_location);
            holder.tv_time = (TextView) convertView.findViewById(R.id.listview_pinglun_item_textView_time);
            holder.tv_lou = (TextView) convertView.findViewById(R.id.listview_pinglun_item_textView_lou);
            holder.tv_content = (TextView) convertView.findViewById(R.id.listview_pinglun_item_textView_content);
            holder.tv_zan = (TextView) convertView.findViewById(R.id.listview_pinglun_item_textView_zan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_lou.setText("第" + (position + 1) + "楼");
        holder.tv_content.setText(news.get(position).getPcontent());

        return convertView;
    }


    private class ViewHolder {
        private TextView tv_name;
        private TextView tv_location;
        private TextView tv_time;
        private TextView tv_lou;
        private TextView tv_content;
        private TextView tv_zan;
        private ImageView iv_icon;
    }

}
