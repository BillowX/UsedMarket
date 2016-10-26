package com.maker.use.ui.view.myXRecyclerView;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.maker.use.R;
import com.maker.use.ui.view.DividerLine;
import com.maker.use.utils.UIUtils;


/**
 * 显示店铺的XRecyclerView
 * Created by XT on 2016/10/9.
 */

public class ShopXRecyclerView extends XRecyclerView {

    private final CoordinatorLayout cl_root;
    private final Context context;

    //刷新时间
    private int refreshTime = 0;

    public ShopXRecyclerView(Context context, CoordinatorLayout cl_root) {
        this(context, null, 0, cl_root);
    }

    public ShopXRecyclerView(Context context, AttributeSet attrs, CoordinatorLayout cl_root) {
        this(context, attrs, 0, cl_root);
    }

    public ShopXRecyclerView(Context context, AttributeSet attrs, int defStyle, CoordinatorLayout cl_root) {
        super(context, attrs, defStyle);
        this.context = context;
        this.cl_root = cl_root;

        initView();
        initData();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        setRefreshProgressStyle(ProgressStyle.BallPulse);
        setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        setArrowImageView(R.drawable.iconfont_downgrey);
        //设置Item增加、移除动画
        setItemAnimator(new DefaultItemAnimator());
        //设置条目之间的分割线
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(0xFFDDDDDD);
        addItemDecoration(dividerLine);
        //设置刷新和加载监听
        setLoadingListener(new LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        refreshComplete();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        loadMoreComplete();
                    }
                }, 2000);
            }
        });
    }

    private void initData() {
    }


}
