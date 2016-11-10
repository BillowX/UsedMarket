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
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private final int SORT_TIME = 0;//按时间排序
    private final int SORT_PRICE = 1;//按价格排序
    private final int SORT_PREFER_NUM = 2;//按收藏数量排序
    @ViewInject(R.id.rl_root)
    RelativeLayout rl_root;
    @ViewInject(R.id.fab_add)
    ImageButton fab_add;
    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.cl_root)
    CoordinatorLayout cl_root;
    //排序
    @ViewInject(R.id.ll_sort_time)
    LinearLayout ll_sort_time;
    @ViewInject(R.id.iv_sort_time)
    ImageView iv_sort_time;
    @ViewInject(R.id.ll_sort_price)
    LinearLayout ll_sort_price;
    @ViewInject(R.id.iv_sort_price)
    ImageView iv_sort_price;
    @ViewInject(R.id.ll_sort_prefer_num)
    LinearLayout ll_sort_prefer_num;
    @ViewInject(R.id.iv_sort_prefer_num)
    ImageView iv_sort_prefer_num;
    private CommodityXRecyclerView mCommodityXRecyclerView;
    private String type;
    private String queryValue;
    private String indistinctField;
    private int CURRENT_SORT = SORT_TIME;//当前的排序
    private boolean ORDER_DOWN = true;//是否为降序

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //开启本activity的动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());
        }
        super.onCreate(savedInstanceState);

        checkWhereFrom();
        initSortView();
        initView();
    }

    private void initSortView() {
        ll_sort_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CURRENT_SORT == SORT_TIME) {
                    ORDER_DOWN = !ORDER_DOWN;
                    if (ORDER_DOWN) {
                        iv_sort_time.setImageResource(R.drawable.sort_down);
                    } else {
                        iv_sort_time.setImageResource(R.drawable.sort_up);
                    }
                } else {
                    CURRENT_SORT = SORT_TIME;//更新当前排序
                    iv_sort_price.setVisibility(View.GONE);
                    iv_sort_prefer_num.setVisibility(View.GONE);
                    iv_sort_time.setVisibility(View.VISIBLE);
                    ORDER_DOWN = true;
                }
                rl_root.removeViewAt(0);
                initView();
            }
        });
        ll_sort_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CURRENT_SORT == SORT_PRICE) {
                    ORDER_DOWN = !ORDER_DOWN;
                    if (ORDER_DOWN) {
                        iv_sort_price.setImageResource(R.drawable.sort_down);
                    } else {
                        iv_sort_price.setImageResource(R.drawable.sort_up);
                    }
                } else {
                    CURRENT_SORT = SORT_PRICE;//更新当前排序
                    iv_sort_price.setVisibility(View.VISIBLE);
                    iv_sort_prefer_num.setVisibility(View.GONE);
                    iv_sort_time.setVisibility(View.GONE);
                    ORDER_DOWN = true;
                }
                rl_root.removeViewAt(0);
                initView();
            }
        });
        ll_sort_prefer_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CURRENT_SORT == SORT_PREFER_NUM) {
                    ORDER_DOWN = !ORDER_DOWN;
                    if (ORDER_DOWN) {
                        iv_sort_price.setImageResource(R.drawable.sort_down);
                    } else {
                        iv_sort_price.setImageResource(R.drawable.sort_up);
                    }
                } else {
                    CURRENT_SORT = SORT_PREFER_NUM;//更新当前排序
                    iv_sort_price.setVisibility(View.GONE);
                    iv_sort_prefer_num.setVisibility(View.VISIBLE);
                    iv_sort_time.setVisibility(View.GONE);
                    ORDER_DOWN = true;
                }
                rl_root.removeViewAt(0);
                initView();
            }
        });
    }

    private void checkWhereFrom() {
        type = getIntent().getStringExtra("type");
        queryValue = getIntent().getStringExtra("queryValue");
        indistinctField = getIntent().getStringExtra("indistinctField");
    }

    private void initView() {
        //添加MyXRecyclerView
        HashMap<String, String> map = new HashMap<>();
        if (ORDER_DOWN) {
            map.put("order", "ASC");
        } else {
            map.put("order", "DESC");
        }
        switch (CURRENT_SORT) {
            case SORT_TIME:
                map.put("orderBy", "launch_date");
                break;
            case SORT_PRICE:
                map.put("orderBy", "price");
                break;
            case SORT_PREFER_NUM:
                map.put("orderBy", "prefer_num");
                break;
            default:
                map.put("orderBy", "");
                break;
        }

        //我的发布页面（indistinctField查询关键字为空的时候）
        if (TextUtils.isEmpty(indistinctField) && "t_commodity.user_id".equals(type)) {
            map.put("type", type);//类型是t_commodity.user_id
            map.put("queryValue", queryValue);//查询值是userId
            //toolbar设置标题必须在setSupportActionBar才能生效
            toolbar.setTitle("我的发布");
            fab_add.setVisibility(View.VISIBLE);
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
                }
            });

        }

        //分类页面
        else if (TextUtils.isEmpty(indistinctField) && "category".equals(type)) {
            map.put("type", type);//类型是category
            map.put("queryValue", queryValue);//查询值是具体的分类名字

            toolbar.setTitle(queryValue);
            fab_add.setVisibility(View.GONE);
        }

        //在“我的发布”中搜索
        else if (!TextUtils.isEmpty(indistinctField) && "t_commodity.user_id".equals(type)) {
            map.put("type", type);//类型是t_commodity.user_id
            map.put("queryValue", queryValue);//查询值是userId
            map.put("indistinctField", indistinctField);//查询的关键字

            toolbar.setTitle("在“我的发布”中搜索“" + indistinctField + "”");
            fab_add.setVisibility(View.GONE);
        }

        //在分类中搜索
        else if (!TextUtils.isEmpty(indistinctField) && "category".equals(type)) {
            map.put("type", type);//类型是category
            map.put("queryValue", queryValue);//查询值是具体的分类名字
            map.put("indistinctField", indistinctField);//查询的关键字

            toolbar.setTitle("在“" + queryValue + "”中搜索“" + indistinctField + "”");
            fab_add.setVisibility(View.GONE);
        }

        //在全部商品中搜索
        else if ("all".equals(type) && !TextUtils.isEmpty(indistinctField)) {
            map.put("type", type);//类型是ALL
            map.put("indistinctField", indistinctField);//查询的关键字

            toolbar.setTitle("在“全部商品”中搜索“" + indistinctField + "”");
            fab_add.setVisibility(View.GONE);
        }

        /*//兴趣
        else if ("hobby".equals(type) && TextUtils.isEmpty(indistinctField)) {
            map.put("type", type);//类型是hobby
            map.put("indistinctField", indistinctField);//查询的关键字

            toolbar.setTitle("专题详情");
            fab_add.setVisibility(View.GONE);
        }*/

        //兴趣搜索
        else if ("hobby".equals(type) && !TextUtils.isEmpty(indistinctField)) {
            map.put("type", type);//类型是ALL
            map.put("indistinctField", indistinctField);//查询的关键字

            toolbar.setTitle("在“专题”中搜索“" + indistinctField + "”");
            fab_add.setVisibility(View.GONE);
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
                intent.putExtra("type", type);
                intent.putExtra("queryValue", queryValue);
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
