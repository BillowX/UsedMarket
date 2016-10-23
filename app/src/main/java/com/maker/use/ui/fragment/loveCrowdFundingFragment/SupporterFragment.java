package com.maker.use.ui.fragment.loveCrowdFundingFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maker.use.R;
import com.maker.use.ui.fragment.BaseFragment;

/**
 * 爱心众筹 支持者界面
 * Created by XT on 2016/10/22.
 */

public class SupporterFragment extends BaseFragment {
    private View mMainView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_love_supporter, null);
        return mMainView;
    }
}
