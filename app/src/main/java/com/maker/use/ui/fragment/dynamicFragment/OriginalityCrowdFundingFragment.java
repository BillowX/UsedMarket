package com.maker.use.ui.fragment.dynamicFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maker.use.R;
import com.maker.use.ui.activity.OriginalityCrowdFundingDetailActivity;
import com.maker.use.ui.adapter.CrowdFundingXRecyclerViewAdapter;
import com.maker.use.ui.fragment.BaseFragment;
import com.maker.use.ui.view.myXRecyclerView.CrowdFundingXRecyclerView;
import com.maker.use.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创意众筹界面
 */
public class OriginalityCrowdFundingFragment extends BaseFragment {

    private CrowdFundingXRecyclerViewAdapter mAdapter;
    private List<String> mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CoordinatorLayout cl_root = (CoordinatorLayout) inflater.inflate(R.layout.viewpage_list_dynamic, container, false);
        CrowdFundingXRecyclerView xRecyclerView = new CrowdFundingXRecyclerView(UIUtils.getContext(), cl_root);
        cl_root.addView(xRecyclerView, 0);
        getDataFromServer();
        setupXRecyclerView(xRecyclerView);

        return cl_root;
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

    private void setupXRecyclerView(CrowdFundingXRecyclerView xRecyclerView) {
        mAdapter = new CrowdFundingXRecyclerViewAdapter(getActivity(), mData);
        xRecyclerView.setAdapter(mAdapter);
        //设置点击监听
        mAdapter.setOnItemClickListener(new CrowdFundingXRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(UIUtils.getContext(), OriginalityCrowdFundingDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

}
