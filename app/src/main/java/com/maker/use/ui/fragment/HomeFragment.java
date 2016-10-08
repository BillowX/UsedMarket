package com.maker.use.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.widget.HeaderScrollHelper;
import com.lzy.widget.HeaderViewPager;
import com.lzy.widget.tab.CircleIndicator;
import com.maker.use.R;
import com.maker.use.domain.Goods;
import com.maker.use.ui.adapter.MyRecyclerViewAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;



/**
 * 主页界面
 * Created by XT on 2016/9/28.
 */

public class HomeFragment extends BaseFragment implements HeaderScrollHelper.ScrollableContainer {

    @ViewInject(R.id.recView_home)
    private XRecyclerView recView_home;

    @ViewInject(R.id.scrollableLayout)
    private HeaderViewPager scrollableLayout;

    @ViewInject(value = R.id.pagerHeader, parentId = R.id.recViewHeader)
    private ViewPager pagerHeader;
    @ViewInject(value = R.id.ci, parentId = R.id.recViewHeader)
    private CircleIndicator ci;

    private List<Goods> mList;
    private MyRecyclerViewAdapter mAdapter;


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_home, null);
        x.view().inject(this, mainView);
        initView();

        return mainView;
    }

    private void initView() {
        pagerHeader.setAdapter(new HeaderAdapter());
        ci.setViewPager(pagerHeader);
        scrollableLayout.setCurrentScrollableContainer(this);
        scrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                //让头部具有差速动画,如果不需要,可以不用设置
                pagerHeader.setTranslationY(currentY / 2);

            }
        });

        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add(new Goods(R.mipmap.image1, "test"));
            mList.add(new Goods(R.mipmap.image2, "test"));
            mList.add(new Goods(R.mipmap.image3, "test"));
            mList.add(new Goods(R.mipmap.image4, "test"));
            mList.add(new Goods(R.mipmap.image5, "test"));
        }

        recView_home.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

//        recView_home.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        mAdapter = new MyRecyclerViewAdapter(mList);
        recView_home.setAdapter(mAdapter);
        recView_home.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 3; i++) {
                            mAdapter.add(0, new Goods(R.mipmap.ic_launcher, "刷新"));
                        }
                        recView_home.refreshComplete();
                    }
                },1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 3; i++) {
                            mAdapter.add(new Goods(R.mipmap.ic_launcher, "加载"));
                        }
                        recView_home.loadMoreComplete();
                    }
                },1000);
            }
        });
    }

    @Override
    public View getScrollableView() {
        return recView_home;
    }


    private class HeaderAdapter extends PagerAdapter {

        public int[] images = new int[]{//
                R.mipmap.image1, R.mipmap.image2, R.mipmap.image3,//
                R.mipmap.image4, R.mipmap.image5};

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(images[position]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}
