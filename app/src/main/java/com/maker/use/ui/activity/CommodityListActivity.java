package com.maker.use.ui.activity;

import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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
    @ViewInject(R.id.deleteBar)
    FrameLayout deleteBar;

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
            deleteBar.setVisibility(View.GONE);

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

            //删除Bar
            deleteBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.toast("删除");
                }
            });

        } else if (!TextUtils.isEmpty(category)) {
            map.put("category", category);
            fab_add.setVisibility(View.GONE);
            deleteBar.setVisibility(View.GONE);
        }

        mMyXRecyclerView = new MyXRecyclerView(UIUtils.getContext(), map);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMyXRecyclerView.setLayoutParams(layoutParams);
        rl_root.addView(mMyXRecyclerView, layoutParams);

        if (!TextUtils.isEmpty(username)) {
            //  为RecyclerView控件设置滚动事件
            mMyXRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    super.onScrolled(recyclerView, dx, dy);
                    //  dx：大于0，向右滚动    小于0，向左滚动
                    //  dy：大于0，向上滚动    小于0，向下滚动

                    if (dy > 20) {
                        //  列表向上滚动，隐藏删除面板
                        if (deleteBar.getVisibility() == View.VISIBLE) {
                            hideDeleteBar();
                        }
                    } else if ((dy < -20)) {
                        // 列表向下滚动，显示删除面板
                        if (deleteBar.getVisibility() == View.GONE) {
                            showDeleteBar();
                        }
                    }

                }
            });
        }
    }


    @Override
    public View getScrollableView() {
        return mMyXRecyclerView;
    }

    private void showDeleteBar() {
        deleteBar.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.translate_up_on));
        deleteBar.setVisibility(View.VISIBLE);
    }

    private void hideDeleteBar() {
        deleteBar.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.translate_up_off));

        deleteBar.setVisibility(View.GONE);
    }
}
