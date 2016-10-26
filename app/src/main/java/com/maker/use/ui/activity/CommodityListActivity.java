package com.maker.use.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.maker.use.R;
import com.maker.use.ui.view.myXRecyclerView.CommodityXRecyclerView;
import com.maker.use.utils.UIUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * 我的发布页面
 * Created by XT on 2016/10/8.
 */
@ContentView(R.layout.activity_commoditylist)
public class CommodityListActivity extends BaseActivity {

    @ViewInject(R.id.rl_root)
    RelativeLayout rl_root;
    @ViewInject(R.id.fab_add)
    ImageButton fab_add;
    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.cl_root)
    CoordinatorLayout cl_root;

    private CommodityXRecyclerView mCommodityXRecyclerView;
    private String mUserId;
    private String mCategory;
    private String mQuery;
    private String mAll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //开启本activity的动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setExitTransition(new Explode());//new Slide()  new Fade()
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());
        }
        super.onCreate(savedInstanceState);

        checkWhereFrom();
        initView();
    }

    private void checkWhereFrom() {
        mUserId = getIntent().getStringExtra("userId");
        mCategory = getIntent().getStringExtra("category");
        mQuery = getIntent().getStringExtra("query");
        mAll = getIntent().getStringExtra("all");
    }

    private void initView() {
        //添加MyXRecyclerView
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(mUserId) && TextUtils.isEmpty(mQuery)) {
            map.put("userId", mUserId);
            //toolbar设置标题必须在setSupportActionBar才能生效
            toolbar.setTitle("我的发布");
            fab_add.setVisibility(View.VISIBLE);
            //将发布按钮绘制成圆形(5.0)
            /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
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
                //设置裁剪
                fab_add.setClipToOutline(true);
                fab_add.setOutlineProvider(viewOutlineProvider);
                //可以判断裁剪状态
//                fab_add.getClipToOutline();
                //可以禁止裁剪状态
//                fab_add.setClipToOutline(false);
            }*/
            //添加按钮点击事件
            fab_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UIUtils.getContext(), AddCommodityActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(CommodityListActivity.this).toBundle());
                    } else {
                        startActivity(intent);
                    }
                    finish();
                   /* //添加测试代码
                    x.http().get(new RequestParams(UsedMarketURL.server_heart + "/servlet/InsertTestDataServlet"), new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            UIUtils.toast("添加成功");
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {

                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });*/
                }
            });

        } else if (!TextUtils.isEmpty(mCategory) && TextUtils.isEmpty(mQuery)) {
            map.put("category", mCategory);
            toolbar.setTitle(mCategory);
            fab_add.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(mUserId) && !TextUtils.isEmpty(mQuery)) {
            toolbar.setTitle("在“我的发布”中搜索“" + mQuery + "”");
            fab_add.setVisibility(View.GONE);
            map.put("userId", mUserId);
            map.put("query", mQuery);
        } else if (!TextUtils.isEmpty(mCategory) && !TextUtils.isEmpty(mQuery)) {
            toolbar.setTitle("在“" + mCategory + "”中搜索“" + mQuery + "”");
            fab_add.setVisibility(View.GONE);
            map.put("category", mCategory);
            map.put("query", mQuery);
        } else if (!TextUtils.isEmpty(mAll) && !TextUtils.isEmpty(mQuery)) {
            toolbar.setTitle("在“全部商品”中搜索“" + mQuery + "”");
            fab_add.setVisibility(View.GONE);
            map.put("all", mAll);
            map.put("query", mQuery);
        }

        mCommodityXRecyclerView = new CommodityXRecyclerView(this, map, cl_root);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mCommodityXRecyclerView.setLayoutParams(layoutParams);
        rl_root.addView(mCommodityXRecyclerView, 0, layoutParams);


        //设置ToolBar
        //取代原本的actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //如果有Menu,创建完后,系统会自动添加到ToolBar上
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_commodity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(UIUtils.getContext(), SearchActivity.class);
                if (!TextUtils.isEmpty(mUserId)) {
                    //说明是在登陆用户发布的商品中查找
                    intent.putExtra("username", mUserId);
                } else if (!TextUtils.isEmpty(mCategory)) {
                    //说明是在登陆用户发布的商品中查找
                    intent.putExtra("category", mCategory);
                } else if (!TextUtils.isEmpty(mAll)) {
                    //说明是在登陆用户全部商品中查找
                    intent.putExtra("all", mAll);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    startActivity(intent);
                }
                finish();
                break;
        }
        return true;
    }
}
