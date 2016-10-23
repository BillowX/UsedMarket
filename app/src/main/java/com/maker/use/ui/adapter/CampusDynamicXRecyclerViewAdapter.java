package com.maker.use.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maker.use.R;
import com.maker.use.ui.activity.CampusDynamicDetailActivity;

import java.util.List;


/**
 * 校园动态列表适配器
 * Created by XT on 2016/10/22.
 */

public class CampusDynamicXRecyclerViewAdapter extends RecyclerView.Adapter<CampusDynamicXRecyclerViewAdapter.MyViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<String> mValues;

    public CampusDynamicXRecyclerViewAdapter(Context context, List<String> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;

        mValues = items;
    }


    @Override
    public CampusDynamicXRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic_campusdynamic, parent, false);
        view.setBackgroundResource(mBackground);
        return new CampusDynamicXRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CampusDynamicXRecyclerViewAdapter.MyViewHolder holder, final int position) {
        Glide.with(holder.iv_img.getContext()).load(R.mipmap.cheese_1).fitCenter().into(holder.iv_img);
        holder.tv_title.setText("校园动态" + position);
        holder.tv_news_detail.setText("今天，我们在这里隆重集会，纪念中国工农红军长征胜利80周年。红军长征的那个年代，中国处在半殖民地半封建社会的黑暗境地，社会危机四伏，日寇野蛮侵略，国民党反动派置民族危亡于不顾，向革命根据地连续发动大规模“围剿”，中国共产党和红军到了危急关头，中国革命到了危急关头，中华民族到了危急关头。");

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CampusDynamicDetailActivity.class);
                intent.putExtra("newsUrl", mValues.get(position));

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final View rootView;
        public final ImageView iv_img;
        public final TextView tv_title;
        public final TextView tv_news_detail;
        public final TextView tv_date;
        public final TextView tv_author;

        public MyViewHolder(View view) {
            super(view);
            rootView = view;
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_news_detail = (TextView) view.findViewById(R.id.tv_news_detail);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_author = (TextView) view.findViewById(R.id.tv_author);
        }
    }
}