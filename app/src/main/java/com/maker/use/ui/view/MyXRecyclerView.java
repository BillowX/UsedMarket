package com.maker.use.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.maker.use.domain.Commodity;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.activity.CommodityDetailActivity;
import com.maker.use.ui.adapter.MyRecyclerViewAdapter;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;


/**
 * 自定义的XRecyclerView
 * Created by XT on 2016/10/9.
 */

public class MyXRecyclerView extends XRecyclerView {

    private HashMap<String, String> map;

    //刷新时间
    private int refreshTime = 0;
    private List<Commodity> mCommoditys;
    private MyRecyclerViewAdapter mAdapter;
    private String mAll;
    private String mUsername;
    private String mCategory;
    private ImageView mErrorImageView;

    public MyXRecyclerView(Context context, HashMap<String, String> map) {
        this(context, null, 0, map);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs, HashMap<String, String> map) {
        this(context, attrs, 0, map);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs, int defStyle, HashMap<String, String> map) {
        super(context, attrs, defStyle);
        this.map = map;

        initView();
        initData();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        setLoadingMoreProgressStyle(ProgressStyle.Pacman);
//      setArrowImageView(R.drawable.iconfont_downgrey);
        //设置刷新和加载监听
        setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mCommoditys.clear();
                        mCommoditys = null;
                        get10CommoditysFromService("0");
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore() {
                get10CommoditysFromService(String.valueOf(mCommoditys.size()));
            }
        });
    }

    private void initData() {
        if (map.get("all") != null) {
            mAll = map.get("all");
        } else if (map.get("username") != null) {
            mUsername = map.get("username");
        } else if (map.get("category") != null) {
            mCategory = map.get("category");
        }
        get10CommoditysFromService("0");


    }

    private void get10CommoditysFromService(String index) {
        RequestParams params = new RequestParams(UsedMarketURL.server_heart + "/servlet/FindCommodityServlet");


        if (!TextUtils.isEmpty(mAll)) {
            params.addQueryStringParameter("all", mAll);
        } else if (!TextUtils.isEmpty(mUsername)) {
            params.addQueryStringParameter("username", mUsername);
        } else if (!TextUtils.isEmpty(mCategory)) {
            params.addQueryStringParameter("category", mCategory);
        }

        params.addQueryStringParameter("index", index);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                final Gson gson = new Gson();
                //刷新逻辑
                if (mCommoditys == null) {
                    mCommoditys = gson.fromJson(result, new TypeToken<List<Commodity>>() {
                    }.getType());
                    //设置适配器
                    mAdapter = new MyRecyclerViewAdapter(mCommoditys);
                    mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, Commodity commodity) {
                            Intent intent = new Intent(UIUtils.getContext(), CommodityDetailActivity.class);
                            intent.putExtra("commodity", commodity);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            UIUtils.getContext().startActivity(intent);

                        }
                    });
                    setAdapter(mAdapter);
                    //如果没有数据，则显示一个空白样式的图片
                    if (mCommoditys.size() < 1) {
                        UIUtils.toast("暂无数据哦");
                        if (mErrorImageView == null) {
//                            mErrorImageView = new ImageView(UIUtils.getContext());
//                            mErrorImageView.setImageResource(R.drawable.empty);
//                            addView(mErrorImageView);
                        }
                    } else {
                        if (mErrorImageView != null) {
//                            removeView(mErrorImageView);
//                            mErrorImageView = null;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    refreshComplete();
                }
                //加载更多逻辑
                else {
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
                            loadMoreComplete();
                        }
                    }, 3000);
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

}
