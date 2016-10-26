package com.maker.use.ui.fragment.homeFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.maker.use.R;
import com.maker.use.ui.adapter.ShopXRecyclerViewAdapter;
import com.maker.use.ui.fragment.BaseFragment;
import com.maker.use.ui.view.myXRecyclerView.ShopXRecyclerView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * 商店界面
 * Created by XT on 2016/10/21.
 */

public class ShopFragment extends BaseFragment {

    private View mMainView;
    @ViewInject(R.id.cl_shop_root)
    private CoordinatorLayout cl_shop_root;
    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_shop, cl_shop_root);
        x.view().inject(this, mMainView);
        initData();
        initView();
        return mMainView;
    }

    private void initData() {
    }

    private void initView() {
        mActivity = getActivity();
        ShopXRecyclerView shopXRecyclerView = new ShopXRecyclerView(mActivity, cl_shop_root);
        ShopXRecyclerViewAdapter adapter = new ShopXRecyclerViewAdapter(mActivity);
        shopXRecyclerView.setAdapter(adapter);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        shopXRecyclerView.setLayoutParams(layoutParams);
        cl_shop_root.addView(shopXRecyclerView, 0, layoutParams);


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
