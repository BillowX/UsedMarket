/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maker.use.ui.fragment.dynamicFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.maker.use.R;
import com.maker.use.ui.adapter.DonationsDynamicRecyclerViewAdapter;
import com.maker.use.ui.view.DividerLine;
import com.maker.use.utils.UIUtils;

/**
 * 捐赠动态
 */
public class DonationsDynamicFragment extends Fragment {

    /**
     * 在上拉刷新的时候，判断，是否处于上拉刷新，如果是的话，就禁止在一次刷新，保障在数据加载完成之前
     * 避免重复和多次加载
     */
    private boolean isLoadMore = true;

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.viewpage_item_dynamic, container, false);
        initView();
        return mRootView;
    }

    private void initView() {
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
        rv_dynamic.setAdapter(new DonationsDynamicRecyclerViewAdapter(getActivity()));


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
