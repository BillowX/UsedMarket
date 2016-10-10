package com.maker.use.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lzy.widget.HeaderScrollHelper;
import com.maker.use.R;
import com.maker.use.ui.view.MyXRecyclerView;
import com.maker.use.utils.UIUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * 我的发布页面
 * Created by XT on 2016/10/8.
 */
@ContentView(R.layout.activity_commoditylist)
public class CommodityListActivity extends BaseActivity implements HeaderScrollHelper.ScrollableContainer {

    @ViewInject(R.id.rl_root)
    RelativeLayout rl_root;

    private MyXRecyclerView mMyXRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        HashMap<String, String> map = new HashMap<>();
        String username = getIntent().getStringExtra("username");
        String category = getIntent().getStringExtra("category");
        if (!TextUtils.isEmpty(username)) {
            map.put("username", username);
        } else if (!TextUtils.isEmpty(category)) {
            map.put("category", category);
        }

        mMyXRecyclerView = new MyXRecyclerView(UIUtils.getContext(), map);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMyXRecyclerView.setLayoutParams(layoutParams);
        rl_root.addView(mMyXRecyclerView, layoutParams);
    }


    @Override
    public View getScrollableView() {
        return mMyXRecyclerView;
    }
}
