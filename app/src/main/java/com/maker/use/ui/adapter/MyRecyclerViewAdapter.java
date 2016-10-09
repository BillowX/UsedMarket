package com.maker.use.ui.adapter;


import android.app.Notification;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.domain.Goods;

import java.util.List;


/**
 * Created by XISEVEN on 2016/9/27.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    List<Goods> goodsList;

    public MyRecyclerViewAdapter(List<Goods> list) {
        this.goodsList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recview_item_home, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.imageView.setImageResource(goodsList.get(position).getImgId());
        holder.textView.setText(goodsList.get(position).getMsg());
    }


    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView){
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.recView_item_home_img );
            textView= (TextView) itemView.findViewById(R.id.recView_item_home_msg);
        }
    }
    public void add(Goods goods){
        if (goodsList != null) {
            goodsList.add(goods);
            notifyItemInserted(goodsList.size());
        }
    }

    public void add(int position, Goods goods) {
        if (goodsList != null) {
            goodsList.add(position, goods);
            notifyItemInserted(position);
        }
    }
}
