package com.maker.use.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.domain.Commodity;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.adapter.GalleryAdapter;
import com.maker.use.ui.view.GalleryView;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.radius;

/**
 * 商品详情页
 * Created by XISEVEN on 2016/10/9.
 */
@ContentView(R.layout.activity_commoditydetail)
public class CommodityDetailActivity extends BaseActivity {
    @ViewInject(R.id.iv_userHeadImg)
    private ImageView iv_userHeadimg;
    @ViewInject(R.id.tv_userName)
    private TextView tv_userName;
    @ViewInject(R.id.tv_goods_price)
    private TextView tv_goods_price;
    @ViewInject(R.id.tv_goods_time)
    private TextView tv_goods_time;
    @ViewInject(R.id.tv_goods_name)
    private TextView tv_goods_name;
    @ViewInject(R.id.iv_img)
    private ImageView iv_img;
    @ViewInject(R.id.recView_goods_img)
    private GalleryView recView_goods_img;
    @ViewInject(R.id.tv_goods_description)
    private TextView tv_goods_description;
    @ViewInject(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Commodity commodity = (Commodity) getIntent().getSerializableExtra("commodity");
        if (commodity != null) {
            //用户头像
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setRadius(DensityUtil.dip2px(radius))
                    .setIgnoreGif(false)
                    .setCrop(true)//是否对图片进行裁剪
                    .setFailureDrawableId(R.drawable.register_default_head)
                    .setLoadingDrawableId(R.drawable.register_default_head)
                    .build();
            x.image().bind(iv_userHeadimg, UsedMarketURL.server_heart + "/head/" + commodity.username + "_head.jpg", imageOptions);
            tv_userName.setText(commodity.username);
            tv_goods_price.setText("¥ " + commodity.price);
            tv_goods_time.setText(commodity.time);
            tv_goods_name.setText(commodity.name);
            tv_goods_description.setText(commodity.description);
//            x.image().bind(iv_img, UsedMarketURL.server_heart + "//" + commodity.imgurl);


            final ArrayList<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(R.drawable.a,
                    R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            recView_goods_img.setLayoutManager(linearLayoutManager);
            GalleryAdapter mAdapter = new GalleryAdapter(this, mDatas);
            recView_goods_img.setAdapter(mAdapter);

            recView_goods_img.setOnItemScrollChangeListener(new GalleryView.OnItemScrollChangeListener() {
                @Override
                public void onChange(View view, int position) {
                    iv_img.setImageResource(mDatas.get(position));
                }

                ;
            });

            mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    iv_img.setImageResource(mDatas.get(position));
                }
            });
        }
    }
}
