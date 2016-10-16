package com.maker.use.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.maker.use.R;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.view.MyXRecyclerView;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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

    private MyXRecyclerView mMyXRecyclerView;
    private String mUsername;
    private String mCategory;
    private String mQuery;
    private String mAll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkWhereFrom();
        initView();
    }

    private void checkWhereFrom() {
        mUsername = getIntent().getStringExtra("username");
        mCategory = getIntent().getStringExtra("category");
        mQuery = getIntent().getStringExtra("query");
        mAll = getIntent().getStringExtra("all");
    }

    private void initView() {
        //添加MyXRecyclerView
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(mUsername) && TextUtils.isEmpty(mQuery)) {
            map.put("username", mUsername);
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
                    startActivity(new Intent(UIUtils.getContext(), AddCommodityActivity.class));

                    //添加测试代码
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
                    });
                }
            });

        } else if (!TextUtils.isEmpty(mCategory) && TextUtils.isEmpty(mQuery)) {
            map.put("category", mCategory);
            toolbar.setTitle(mCategory);
            fab_add.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(mUsername) && !TextUtils.isEmpty(mQuery)) {
            toolbar.setTitle("在“我的发布”中搜索“" + mQuery + "”");
            fab_add.setVisibility(View.GONE);
            map.put("username", mUsername);
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

        mMyXRecyclerView = new MyXRecyclerView(this, map, cl_root);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMyXRecyclerView.setLayoutParams(layoutParams);
        rl_root.addView(mMyXRecyclerView, 0, layoutParams);


        //设置ToolBar
        //取代原本的actionbar
        setSupportActionBar(toolbar);
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
                if (!TextUtils.isEmpty(mUsername)) {
                    //说明是在登陆用户发布的商品中查找
                    intent.putExtra("username", mUsername);
                } else if (!TextUtils.isEmpty(mCategory)) {
                    //说明是在登陆用户发布的商品中查找
                    intent.putExtra("category", mCategory);
                } else if (!TextUtils.isEmpty(mAll)) {
                    //说明是在登陆用户全部商品中查找
                    intent.putExtra("all", mAll);
                }
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
