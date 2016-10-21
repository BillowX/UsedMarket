package com.maker.use.ui.fragment.homeFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maker.use.R;
import com.maker.use.ui.fragment.BaseFragment;

import org.xutils.x;

/**
 * 商店界面
 * Created by XT on 2016/10/21.
 */

public class ShopFragment extends BaseFragment {

    private View mMainView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_shop, null);
        x.view().inject(this, mMainView);

        initView();
        return mMainView;
    }

    private void initView() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // 页面正在展示,在这里加载你的数据
//            UIUtils.snackBar(mMainView,"展示");
        }else{
            // 页面没有展示
        }
    }
}
