package com.maker.use.ui.activity;

import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
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
    @ViewInject(R.id.fab_add)
    ImageButton fab_add;

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
            fab_add.setVisibility(View.VISIBLE);

            //将发布按钮绘制成圆形
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {

                    @Override
                    public void getOutline(View view, Outline outline) {
                        // 获取按钮的尺寸
                        int fabSize = getResources().getDimensionPixelSize(
                                R.dimen.fab_size);
                        // 设置轮廓的尺寸
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            outline.setOval(-4, -4, fabSize + 2, fabSize + 2);
                        }

                    }
                };
                fab_add.setOutlineProvider(viewOutlineProvider);
            }
            fab_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UIUtils.getContext(), UploadCommodityActivity.class));
                }
            });

        } else if (!TextUtils.isEmpty(category)) {
            map.put("category", category);
            fab_add.setVisibility(View.GONE);
        }

        mMyXRecyclerView = new MyXRecyclerView(UIUtils.getContext(), map);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMyXRecyclerView.setLayoutParams(layoutParams);
        rl_root.addView(mMyXRecyclerView, 0, layoutParams);

    }

    @Override
    public View getScrollableView() {
        return mMyXRecyclerView;
    }

}
