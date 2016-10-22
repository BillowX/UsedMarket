package com.maker.use.ui.fragment.originalityCrowdFundingDetailFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maker.use.R;
import com.maker.use.ui.fragment.BaseFragment;

/**
 * 项目主页
 * Created by XT on 2016/10/22.
 */

public class HomepageFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_originality_homepage, null);
        return view;
    }
}
