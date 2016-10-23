package com.maker.use.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maker.use.R;

/**
 * 数据为空时的RecyclerView的条目适配器
 * Created by XT on 2016/10/23.
 */
public class EmptyAdapter extends RecyclerView.Adapter<EmptyAdapter.MyEmptyViewHolder> {

    @Override
    public MyEmptyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_commoditylist_empty, parent, false);
        MyEmptyViewHolder holder = new MyEmptyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyEmptyViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class MyEmptyViewHolder extends RecyclerView.ViewHolder {
        public MyEmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
