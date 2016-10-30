package com.maker.use.ui.fragment.dynamicFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.maker.use.R;
import com.maker.use.ui.activity.OriginalityCrowdFundingDetailActivity;
import com.maker.use.ui.adapter.CrowdFundingRecyclerViewAdapter;
import com.maker.use.ui.fragment.BaseFragment;
import com.maker.use.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创意众筹界面
 */
public class OriginalityCrowdFundingFragment extends BaseFragment {

    /**
     * 在上拉刷新的时候，判断，是否处于上拉刷新，如果是的话，就禁止在一次刷新，保障在数据加载完成之前
     * 避免重复和多次加载
     */
    private boolean isLoadMore = true;

    private List<String> mData;
    private Activity mActivity;
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.viewpage_item_dynamic, container, false);

        getDataFromServer();
        initView();

        return mRootView;
    }

    /**
     * 从服务器上获取数据
     */
    protected void getDataFromServer() {
        mData = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mData.add("" + (char) i);
        }
    }

    private void initView() {
        mActivity = getActivity();

        MaterialRefreshLayout refresh_root = (MaterialRefreshLayout) mRootView.findViewById(R.id.refresh_root);
        RecyclerView rv_dynamic = (RecyclerView) mRootView.findViewById(R.id.rv_dynamic);

        //设置RecyclerView
        //设置为瀑布流
        rv_dynamic.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //下面可以自己设置默认动画
        rv_dynamic.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        CrowdFundingRecyclerViewAdapter adapter = new CrowdFundingRecyclerViewAdapter(mActivity,mData);
        //设置点击监听
        adapter.setOnItemClickListener(new CrowdFundingRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(UIUtils.getContext(), OriginalityCrowdFundingDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
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

}
