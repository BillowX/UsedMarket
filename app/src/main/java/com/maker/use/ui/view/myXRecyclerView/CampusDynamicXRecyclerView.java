package com.maker.use.ui.view.myXRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.maker.use.R;
import com.maker.use.domain.Commodity;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.activity.CommodityDetailActivity;
import com.maker.use.ui.adapter.CommodityXRecyclerViewAdapter;
import com.maker.use.ui.adapter.EmptyAdapter;
import com.maker.use.ui.view.DividerLine;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


/**
 * 显示校园动态的XRecyclerView
 * Created by XT on 2016/10/9.
 */

public class CampusDynamicXRecyclerView extends XRecyclerView implements View.OnClickListener {

    private final CoordinatorLayout cl_root;
    private final Context context;

    //刷新时间
    private int refreshTime = 0;
    private List<Commodity> mCommodityList;
    private CommodityXRecyclerViewAdapter mAdapter;
    private PopupWindow mPopupWindow;
    //判断是不是刷新逻辑，如果不是，说明第一次进入，那么显示加载中对话框
    private boolean isRefresh = false;

    public CampusDynamicXRecyclerView(Context context, CoordinatorLayout cl_root) {
        this(context, null, 0, cl_root);
    }

    public CampusDynamicXRecyclerView(Context context, AttributeSet attrs, CoordinatorLayout cl_root) {
        this(context, attrs, 0, cl_root);
    }

    public CampusDynamicXRecyclerView(Context context, AttributeSet attrs, int defStyle, CoordinatorLayout cl_root) {
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
//        get10DataFromService("0");
    }

    private void get10DataFromService(String index) {
        if (!isRefresh) {
            UIUtils.progressDialog(context);
        }
        final RequestParams params = new RequestParams(UsedMarketURL.server_heart + "/servlet/FindCommodityServlet");
        params.addQueryStringParameter("index", index);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                final Gson gson = new Gson();
                //刷新逻辑
                if (mCommodityList == null) {
                    mCommodityList = gson.fromJson(result, new TypeToken<List<Commodity>>() {
                    }.getType());

                    //如果没有数据，则显示一个空白样式的图片
                    if (mCommodityList.size() < 1) {
                        EmptyAdapter emptyAdapter = new EmptyAdapter();
                        setAdapter(emptyAdapter);
                    } else {
                        //设置适配器
                        mAdapter = new CommodityXRecyclerViewAdapter(mCommodityList);
                        //设置条目点击监听
                        mAdapter.setOnItemClickListener(new CommodityXRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(final View view, final Commodity commodity) {
                                Intent intent = new Intent(UIUtils.getContext(), CommodityDetailActivity.class);
                                intent.putExtra("commodity", commodity);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    UIUtils.getContext().startActivity(intent);
//                                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, view, "sharp").toBundle());
                                } else {
                                    UIUtils.getContext().startActivity(intent);
                                }
                            }
                        });
                        setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
                    }
                    refreshComplete();

                }
                //加载更多逻辑
                else {
                    UIUtils.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<Commodity> newData = (List<Commodity>) gson.fromJson(result, new TypeToken<List<Commodity>>() {
                            }.getType());
                            if (newData.size() >= 1) {
                                mAdapter.add(newData);
//                                mCommodityList.addAll(newData);
                            } else {
                                UIUtils.snackBar(cl_root, "没有更多咯");
                            }
//                            mAdapter.notifyDataSetChanged();
                            loadMoreComplete();
                        }
                    }, 3000);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.toast("网络出错啦");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (!isRefresh) {
                    UIUtils.closeProgressDialog();
                    isRefresh = true;
                }
            }
        });
    }

    /**
     * 显示删除按钮的PopupWindow
     */
    protected void showPopupWindow(View view, final Commodity commodity, final int position) {
        View popupWindowView = View.inflate(UIUtils.getContext(), R.layout.popupwindow_commodity_item,
                null);
        Button bt_delete = (Button) popupWindowView
                .findViewById(R.id.bt_delete);

        bt_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                UIUtils.progressDialog(context);
                RequestParams params1 = new RequestParams(UsedMarketURL.server_heart + "/servlet/DeleteCommodityServlet");
                params1.addQueryStringParameter("id", String.valueOf(commodity.commodityId));
                x.http().get(params1, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if ("删除成功".equals(result)) {
                            if (mCommodityList != null) {
                                mCommodityList.remove(position);
                                if (mCommodityList.size() < 1) {
                                    mCommodityList.clear();
                                    mCommodityList = null;
                                    get10DataFromService("0");
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        UIUtils.toast(result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        UIUtils.toast("网络出错啦");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        UIUtils.closeProgressDialog();

                    }
                });
                if (mPopupWindow != null) {

                }
            }
        });

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        popupWindowView.setAnimation(animationSet);

        mPopupWindow = new PopupWindow(popupWindowView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//        mPopupWindow.showAsDropDown(view);

    }

    @Override
    public void onClick(View v) {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

}
