package com.maker.use.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.maker.use.R;

/**
 * Glide加载图片工具
 * Created by XT on 2016/10/26.
 */

public class GlideUtils {

    public static void setImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.loading)//加载中图片
                .crossFade()//渐现效果
                .error(R.drawable.error)//错误图片
                .into(imageView);
    }

    public static void setCircleImageViewImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .centerCrop()
                .crossFade()//渐现效果
                .into(imageView);
    }
}
