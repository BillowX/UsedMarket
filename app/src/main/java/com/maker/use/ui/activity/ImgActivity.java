package com.maker.use.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maker.use.R;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.UIUtils;


/**
 * 应用截图界面
 * Created by XT on 2016/9/17.
 */
public class ImgActivity extends Activity {

    private ViewPager mVp_screenshot;
    private String[] mNewImgUrl;
    private int mCurrentPageIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        mNewImgUrl = getIntent().getStringArrayExtra("imgUrl");
        mCurrentPageIndex = getIntent().getIntExtra("index", 0);

        initView();
    }

    private void initView() {
        mVp_screenshot = (ViewPager) findViewById(R.id.vp_screenshot);
        mVp_screenshot.setAdapter(new MyPagerAdapter());

        //设置为点击过来的那张页面
        mVp_screenshot.setCurrentItem(mCurrentPageIndex);
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mNewImgUrl.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(UIUtils.getContext());
            /*FrescoZoomImageView frescoZoomImageView = new FrescoZoomImageView(UIUtils.getContext());
            frescoZoomImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            frescoZoomImageView.loadView(UsedMarketURL.server_heart + "//" + mNewImgUrl[position].replace("_", ""),R.drawable.loading);*/
            /*//设置ImageView的填充类型
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.FIT_XY)
                    .setIgnoreGif(false)
                    .setFailureDrawableId(R.drawable.error)
                    .setLoadingDrawableId(R.drawable.loading)
                    .build();
            x.image().bind(imageView, UsedMarketURL.server_heart + "//" + mNewImgUrl[position].replace("_",""), imageOptions);*/
            GlideUtils.setImg(ImgActivity.this, UsedMarketURL.server_heart + "//" + mNewImgUrl[position].replace("_", ""), imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
