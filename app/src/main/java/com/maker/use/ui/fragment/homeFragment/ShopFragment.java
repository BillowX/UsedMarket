package com.maker.use.ui.fragment.homeFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.maker.use.R;
import com.maker.use.ui.adapter.ShopXRecyclerViewAdapter;
import com.maker.use.ui.fragment.BaseFragment;
import com.maker.use.ui.view.DividerLine;
import com.maker.use.utils.UIUtils;


/**
 * 商店界面
 * Created by XT on 2016/10/21.
 */

public class ShopFragment extends BaseFragment {

    /**
     * 在上拉刷新的时候，判断，是否处于上拉刷新，如果是的话，就禁止在一次刷新，保障在数据加载完成之前
     * 避免重复和多次加载
     */
    private boolean isLoadMore = true;

    private Activity mActivity;
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_shop, container, false);
        initView();
        return mRootView;
    }

    private void initView() {
        mActivity = getActivity();

        MaterialRefreshLayout refresh_root = (MaterialRefreshLayout) mRootView.findViewById(R.id.refresh_root);
        RecyclerView rv_dynamic = (RecyclerView) mRootView.findViewById(R.id.rv_dynamic);

        //设置RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_dynamic.setLayoutManager(layoutManager);
        //下面可以自己设置默认动画
        rv_dynamic.setItemAnimator(new DefaultItemAnimator());
        //设置条目之间的分割线
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(0xFFDDDDDD);
        rv_dynamic.addItemDecoration(dividerLine);
        //设置适配器
        ShopXRecyclerViewAdapter adapter = new ShopXRecyclerViewAdapter(mActivity);
        rv_dynamic.setAdapter(adapter);


        //设置MaterialRefreshLayout
        refresh_root.setLoadMore(isLoadMore);
        refresh_root.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新...
                UIUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.toast("刷新完成");
                        // 结束下拉刷新...
                        materialRefreshLayout.finishRefresh();
                    }
                }, 3000);


            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                //上拉加载更多...
                UIUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.toast("加载完成");
                        // 结束上拉加载更多...
                        materialRefreshLayout.finishRefreshLoadMore();
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // 页面正在展示,在这里加载你的数据
//            UIUtils.snackBar(mMainView,"展示");
        } else {
            // 页面没有展示
        }
    }
}
