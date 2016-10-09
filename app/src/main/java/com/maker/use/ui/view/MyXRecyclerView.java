package com.maker.use.ui.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.maker.use.domain.Commodity;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.adapter.MyRecyclerViewAdapter;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


/**
 * 自定义的XRecyclerView
 * Created by XT on 2016/10/9.
 */

public class MyXRecyclerView extends XRecyclerView {

    //刷新时间
    private int refreshTime = 0;
    private List<Commodity> mCommoditys;
    private MyRecyclerViewAdapter mAdapter;

    public MyXRecyclerView(Context context) {
        this(context, null, 0);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

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
                        get10CommoditysFromService(0);
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore() {
                get10CommoditysFromService(mCommoditys.size());
            }
        });
    }

    private void initData() {
        get10CommoditysFromService(0);
    }

    private void get10CommoditysFromService(int index) {
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
                        setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();
                        refreshComplete();
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
                                loadMoreComplete();
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

}
