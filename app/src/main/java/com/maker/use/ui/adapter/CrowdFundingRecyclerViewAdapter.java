package com.maker.use.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 众筹列表适配器
 * Created by XT on 2016/10/22.
 */

public class CrowdFundingRecyclerViewAdapter extends RecyclerView.Adapter<CrowdFundingRecyclerViewAdapter.MyViewHolder> {

    public OnItemClickListener mOnItemClickListener;
    private List<String> mData;
    private LayoutInflater mInflater;
    private List<Integer> mHeights;

    public CrowdFundingRecyclerViewAdapter(Context context, List<String> datas) {
        mInflater = LayoutInflater.from(context);
        mData = datas;

        mHeights = new ArrayList<Integer>();
        for (int i = 0; i < mData.size(); i++) {
            mHeights.add((int) (300 + Math.random() * 50));
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_dynamic_originalitycrowdfunding, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //设置随机的高度实现瀑布流的效果
        ViewGroup.LayoutParams lp = holder.ll_root.getLayoutParams();
        lp.height = UIUtils.dip2px(mHeights.get(position));
        holder.ll_root.setLayoutParams(lp);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    removeData(pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void removeData(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 单击和长击接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_root;
        ImageView iv_img;
        TextView tv_target;
        TextView tv_have;
        TextView tv_remaining_time;
        TextView tv_support_number;
        TextView tv_title;

        public MyViewHolder(View view) {
            super(view);
            ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_target = (TextView) view.findViewById(R.id.tv_target);
            tv_have = (TextView) view.findViewById(R.id.tv_have);
            tv_remaining_time = (TextView) view.findViewById(R.id.tv_remaining_time);
            tv_support_number = (TextView) view.findViewById(R.id.tv_support_number);

        }
    }

}