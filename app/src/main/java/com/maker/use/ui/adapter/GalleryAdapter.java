package com.maker.use.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.maker.use.R;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.UIUtils;

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private OnItemClickLitener mOnItemClickLitener;
    private LayoutInflater mInflater;
    private String[] mDatas;

    public GalleryAdapter(Context context, String[] datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getItemCount() {
        return mDatas.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.gallery_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        /*ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setIgnoreGif(false)
                .setFailureDrawableId(R.drawable.error)
                .setLoadingDrawableId(R.drawable.loading)
                .setSize(0,0)
                .build();
        x.image().bind(viewHolder.mImg, UsedMarketURL.server_heart + "//" + mDatas[i], imageOptions);*/
        Glide.with(UIUtils.getContext()).load(UsedMarketURL.server_heart + "//" + mDatas[i]).centerCrop().into(viewHolder.mImg);

        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new OnClickListener() {
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

        public ViewHolder(View arg0) {
            super(arg0);

            mImg = (ImageView) arg0
                    .findViewById(R.id.id_index_gallery_item_image);
        }
    }

}
