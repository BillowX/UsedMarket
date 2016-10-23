package com.maker.use.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.utils.UIUtils;


/**
 * 捐赠动态列表适配器
 * Created by XT on 2016/10/22.
 */

public class DonationsDynamicXRecyclerViewAdapter extends RecyclerView.Adapter<DonationsDynamicXRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;

    public DonationsDynamicXRecyclerViewAdapter(Context context) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic_donationsdynamic, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.snackBar(v, "后台维护中，敬请期待哦~");
            }
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View rootView;
        public final TextView tv_name;
        public final TextView tv_description;
        ImageView iv_img0;
        ImageView iv_img1;
        ImageView iv_img2;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            tv_name = (TextView) view.findViewById(R.id.tv_title);
            tv_description = (TextView) view.findViewById(R.id.tv_description);
            iv_img0 = (ImageView) view.findViewById(R.id.iv_img0);
            iv_img1 = (ImageView) view.findViewById(R.id.iv_img1);
            iv_img2 = (ImageView) view.findViewById(R.id.iv_img2);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}