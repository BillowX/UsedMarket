package com.maker.use.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.widget.HeaderScrollHelper;
import com.lzy.widget.tab.CircleIndicator;
import com.maker.use.R;
import com.maker.use.domain.Commodity;
import com.maker.use.domain.Top;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.adapter.MyRecyclerViewAdapter;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页界面
 * Created by XT on 2016/9/28.
 */

public class HomeFragment extends BaseFragment implements HeaderScrollHelper.ScrollableContainer {

    @ViewInject(R.id.rv_home)
    private XRecyclerView rv_home;

    @ViewInject(R.id.pagerHeader)
    private ViewPager pagerHeader;
    @ViewInject(R.id.ci)
    private CircleIndicator ci;

    private MyRecyclerViewAdapter mAdapter;

    private ArrayList<Top.img> mImgs;
    private List<Commodity> mCommoditys;

    //刷新时间
    private int refreshTime = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_home, null);
        x.view().inject(this, mainView);

        initView();
        getDataFromServer();
        initData();
        return mainView;
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_home.setLayoutManager(layoutManager);
        rv_home.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rv_home.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
//        rv_home.setArrowImageView(R.drawable.iconfont_downgrey);
        //添加头布局
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_header_home, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
        x.view().inject(this, header);
        rv_home.addHeaderView(header);
        //设置刷新和加载监听
        rv_home.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mCommoditys.clear();
                        mCommoditys = null;
                        get5CommoditysFromService(0);
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore() {
                get5CommoditysFromService(mCommoditys.size());
            }
        });
    }

    private void initData() {
        get5CommoditysFromService(0);
    }

    private void get5CommoditysFromService(int index) {
        RequestParams params = new RequestParams(UsedMarketURL.server_heart + "/servlet/FindCommodityServlet");
        params.addQueryStringParameter("index", String.valueOf(index));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                if ("无数据".equals(result)) {
                    UIUtils.toast(result);
                } else {
                    final Gson gson = new Gson();
                    if (mCommoditys == null) {
                        mCommoditys = gson.fromJson(result, new TypeToken<List<Commodity>>() {
                        }.getType());
                        //设置适配器
                        mAdapter = new MyRecyclerViewAdapter(mCommoditys);
                        rv_home.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();
                        rv_home.refreshComplete();
                    } else {
                        UIUtils.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                List<Commodity> newData = (List<Commodity>) gson.fromJson(result, new TypeToken<List<Commodity>>() {
                                }.getType());
                                if (newData.size() >= 1) {
                                    mCommoditys.addAll(newData);
                                } else {
                                    UIUtils.toast("没有更多咯");
                                }
                                mAdapter.notifyDataSetChanged();
                                rv_home.loadMoreComplete();
                            }
                        }, 3000);
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.toast("网络出错啦");
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
        return rv_home;
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