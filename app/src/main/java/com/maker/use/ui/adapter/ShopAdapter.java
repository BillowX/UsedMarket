package com.maker.use.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maker.use.R;

/**
 * Created by XT on 2016/10/18.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private GalleryAdapter.OnItemClickLitener mOnItemClickLitener;
    private LayoutInflater mInflater;

    public ShopAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickLitener(GalleryAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_shop_item,
                viewGroup, false);
        ShopAdapter.ViewHolder viewHolder = new ShopAdapter.ViewHolder(view);

        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.id_index_gallery_item_image);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ShopAdapter.ViewHolder viewHolder, final int i) {
//        viewHolder.mImg.setImageResource(mDatas.[i]);

        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }

    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView mTxt;

        public ViewHolder(View arg0) {
            super(arg0);
        }
    }
}
