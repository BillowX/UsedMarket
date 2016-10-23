package com.maker.use.ui.fragment.dynamicFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.maker.use.R;
import com.maker.use.ui.activity.LoveCrowdFundingDetailActivity;
import com.maker.use.ui.adapter.CrowdFundingXRecyclerViewAdapter;
import com.maker.use.ui.view.myXRecyclerView.CrowdFundingXRecyclerView;
import com.maker.use.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 爱心众筹页面
 */
public class LoveCrowdFundingFragment extends Fragment {

    private CrowdFundingXRecyclerViewAdapter mAdapter;
    private List<String> mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CoordinatorLayout cl_root = (CoordinatorLayout) inflater.inflate(R.layout.viewpage_list_dynamic, container, false);
        RelativeLayout rl_root = (RelativeLayout) cl_root.findViewById(R.id.rl_root);
        CrowdFundingXRecyclerView xRecyclerView = new CrowdFundingXRecyclerView(UIUtils.getContext(), cl_root);
        rl_root.addView(xRecyclerView, 0);
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
                Intent intent = new Intent(UIUtils.getContext(), LoveCrowdFundingDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }
}
