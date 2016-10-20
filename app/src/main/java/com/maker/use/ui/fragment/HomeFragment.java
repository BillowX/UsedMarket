package com.maker.use.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lzy.widget.HeaderScrollHelper;
import com.lzy.widget.loop.LoopViewPager;
import com.lzy.widget.tab.CircleIndicator;
import com.maker.use.R;
import com.maker.use.domain.Top;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.activity.MainActivity;
import com.maker.use.ui.activity.SearchActivity;
import com.maker.use.ui.adapter.GalleryAdapter;
import com.maker.use.ui.adapter.ShopAdapter;
import com.maker.use.ui.view.MyXRecyclerView;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 主页界面
 * Created by XT on 2016/9/28.
 */

public class HomeFragment extends BaseFragment implements HeaderScrollHelper.ScrollableContainer {

    @ViewInject(R.id.cl_root)
    CoordinatorLayout cl_root;
    @ViewInject(R.id.pagerHeader)
    private LoopViewPager pagerHeader;
    @ViewInject(R.id.ci)
    private CircleIndicator ci;
    @ViewInject(R.id.ll_find)
    private LinearLayout ll_find;
    @ViewInject(R.id.iv_head)
    private CircleImageView iv_head;
    @ViewInject(R.id.iv_setting)
    private CircleImageView iv_setting;

    private ArrayList<Top.img> mImgs;
    private MyXRecyclerView mMyXRecyclerView;
    private MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_home, null);
        x.view().inject(this, mainView);

        initView();

        return mainView;
    }

    public void initView() {
        mActivity = (MainActivity) getActivity();
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.getDrawerLayout().openDrawer(Gravity.LEFT);
            }
        });
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.getDrawerLayout().openDrawer(Gravity.RIGHT);
            }
        });

        final MainActivity activity = (MainActivity) getActivity();
        activity.setOnFragmentChangeListener(new MainActivity.onFragmentChangeListener() {
            @Override
            public void onFragmentChange() {
                pagerHeader.setAutoLoop(false, 0);
            }

            @Override
            public void onFragmentIsHomeFragment() {
                pagerHeader.setAutoLoop(true, 3000);
            }
        });

        //添加MyXRecyclerView
        HashMap<String, String> map = new HashMap<>();
        map.put("all", "all");
        mMyXRecyclerView = new MyXRecyclerView(activity, map, cl_root);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMyXRecyclerView.setLayoutParams(layoutParams);
        cl_root.addView(mMyXRecyclerView, 0, layoutParams);

        //添加头布局（轮播图)
        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_header_home, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
        x.view().inject(this, headerView);
        getDataFromServer();
        mMyXRecyclerView.addHeaderView(headerView);

        //添加头布局（店铺）
        RecyclerView recyclerView = new RecyclerView(activity);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 4));
        ShopAdapter shopAdapter = new ShopAdapter(activity);
        recyclerView.setAdapter(shopAdapter);
        shopAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                UIUtils.snackBar(view, "招商中，敬请期待...");
            }
        });
        mMyXRecyclerView.addHeaderView(recyclerView);
        //添加监听，在用户滑动到下面时停止图片轮播，节省ui刷新
        mMyXRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View newView = mMyXRecyclerView.getChildAt(1);

                if (newView != null && newView != headerView) {
                    pagerHeader.setAutoLoop(false, 0);
                } else {
                    pagerHeader.setAutoLoop(true, 3000);
                }
            }
        });

        ll_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UIUtils.getContext(), SearchActivity.class);
                intent.putExtra("all", "all");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
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
        pagerHeader.setAdapter(new HeaderAdapter());
        ci.setViewPager(pagerHeader);
    }

    @Override
    public View getScrollableView() {
        return mMyXRecyclerView;
    }

    @Override
    public void onStop() {
        super.onStop();
        pagerHeader.setAutoLoop(false, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        pagerHeader.setAutoLoop(true, 3000);
    }


    private class HeaderAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            x.image().bind(imageView, UsedMarketURL.url_heart + mImgs.get(position).imgUrl);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mImgs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}