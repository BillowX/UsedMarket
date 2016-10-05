package com.maker.use.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maker.use.R;
import com.maker.use.domain.Top;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * 主页界面
 * Created by XT on 2016/9/28.
 */

public class HomeFragment extends BaseFragment {
    //R.layout.fragment_home
    @ViewInject(R.id.lv_home)
    private ListView lv_home;

    //R.layout.list_item_header_home
    @ViewInject(R.id.rl_root)
    private RelativeLayout rl_root;
    @ViewInject(R.id.vp_top)
    private ViewPager vp_top;
    @ViewInject(R.id.tv_top_title)
    private TextView tv_top_title;

    private LinearLayout mLinearLayout;
    // 上个圆点位置
    private int mPreviousPos = 0;
    private ArrayList<Top.img> mImgs;
    private HomeHeaderTask mTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_home, null);
        x.view().inject(this, mainView);
        mPreviousPos = 0;
        getDataFromServer();
        initView();
        return mainView;
    }

    private void initView() {
        //填充头部的轮播图页面（添加到LV的头部）
        View headerView = View.inflate(UIUtils.getContext(), R.layout.list_item_header_home, null);
        x.view().inject(this, headerView);
        lv_home.addHeaderView(headerView);

        //添加一个装指示器的LinearLayout，在获取数据完后去添加相应个数的圆点
        mLinearLayout = new LinearLayout(UIUtils.getContext());
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(llParams);
        // 设置内边距
        int padding = UIUtils.dip2px(5);
        mLinearLayout.setPadding(padding, padding, padding, padding);
        // 添加规则, 设定展示位置
        llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);// 底部对齐
        llParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);// 右对齐
        // 添加布局
        rl_root.addView(mLinearLayout, llParams);

        MyLvAdapter myLvAdapter = new MyLvAdapter();
        lv_home.setAdapter(myLvAdapter);
    }

    /**
     * 从服务器上获取数据
     */
    private void getDataFromServer() {
        //获取TOP10图片的地址
        String url = UsedMarketURL.VPImgUrl;
        x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.toast("网络出错啦~");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析从服务器获取的JSON数据
     *
     * @param result
     */
    private void processData(String result) {
        Gson gson = new Gson();
        Top top = gson.fromJson(result, Top.class);
        mImgs = top.imgs;
        initData();
    }

    /**
     * 在解析完数据后再初始化数据
     */
    private void initData() {
        if (mImgs != null) {
            // 初始化圆点指示器（根据图片的个数）
            for (int i = 0; i < mImgs.size(); i++) {
                ImageView point = new ImageView(UIUtils.getContext());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                // 第一个默认选中(通过改变图片资源来表示选中状态)
                if (i == 0) {
                    point.setImageResource(R.drawable.indicator_selected);
                } else {
                    point.setImageResource(R.drawable.indicator_normal);

                    params.leftMargin = UIUtils.dip2px(4);// 左边距
                }

                point.setLayoutParams(params);
                mLinearLayout.addView(point);
            }

            vp_top.setAdapter(new MyVpAdapter());
            vp_top.setCurrentItem(mImgs.size() * 10000);
            tv_top_title.setText(mImgs.get(0).title);

            //给ViewPager设置页面改变监听，改变圆点选中状态
            vp_top.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    position = position % mImgs.size();
                    tv_top_title.setText(mImgs.get(position).title);

                    // 当前点被选中
                    ImageView point = (ImageView) mLinearLayout.getChildAt(position);
                    point.setImageResource(R.drawable.indicator_selected);
                    if (position != mPreviousPos) {
                        // 上个点变为不选中
                        ImageView prePoint = (ImageView) mLinearLayout
                                .getChildAt(mPreviousPos);
                        prePoint.setImageResource(R.drawable.indicator_normal);
                        //更新上个选中点的值
                        mPreviousPos = position;
                    }
                }

                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            //设置用户点击时不自动轮播
            vp_top.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            UIUtils.getHandler().removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            mTask.start();
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            mTask.start();
                            break;
                    }
                    return false;
                }
            });

            //启动轮播条自动播放(通过handler的postDelayed方法)
            mTask = new HomeHeaderTask();
            mTask.start();
        }
    }

    /**
     * 开启自动轮播的任务线程
     */
    class HomeHeaderTask implements Runnable {

        public void start() {
            // 移除之前发送的所有消息, 避免消息重复
            UIUtils.getHandler().removeCallbacksAndMessages(null);
            UIUtils.getHandler().postDelayed(this, 3000);
        }

        @Override
        public void run() {
            int currentItem = vp_top.getCurrentItem();
            currentItem++;
            vp_top.setCurrentItem(currentItem);

            // 继续发延时3秒消息, 实现内循环
            UIUtils.getHandler().postDelayed(this, 3000);
        }

    }

    //viewPager的适配器
    class MyVpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mImgs.size();

            ImageView imageView = new ImageView(UIUtils.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            x.image().bind(imageView, UsedMarketURL.url_heart + mImgs.get(position).imgUrl);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class MyLvAdapter extends BaseAdapter {

        private final ArrayList<String> mStrings;

        MyLvAdapter() {
            mStrings = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                mStrings.add(i + "");
            }
        }

        @Override
        public int getCount() {
            return mStrings.size();
        }

        @Override
        public Object getItem(int position) {
            return mStrings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(UIUtils.getContext());
            textView.setText(getItem(position).toString());
            return textView;
        }
    }

}
