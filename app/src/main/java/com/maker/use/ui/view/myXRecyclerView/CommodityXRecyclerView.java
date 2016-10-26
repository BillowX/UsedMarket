package com.maker.use.ui.view.myXRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.maker.use.R;
import com.maker.use.domain.Commodity;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.activity.CommodityDetailActivity;
import com.maker.use.ui.adapter.CommodityXRecyclerViewAdapter;
import com.maker.use.ui.adapter.EmptyAdapter;
import com.maker.use.utils.GsonUtils;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;


/**
 * 显示商品的XRecyclerView
 * Created by XT on 2016/10/9.
 */

public class CommodityXRecyclerView extends XRecyclerView implements View.OnClickListener {

    private final CoordinatorLayout cl_root;
    private final Context context;
    private HashMap<String, String> map;

    //刷新时间
    private int refreshTime = 0;
    private List<Commodity> mCommodityList;
    private CommodityXRecyclerViewAdapter mAdapter;
    private String mAll;
    private String mUserId;
    private String mCategory;
    private PopupWindow mPopupWindow;
    private String mQuery;
    //判断是不是刷新逻辑，如果不是，说明第一次进入，那么显示加载中对话框
    private boolean isRefresh = false;

    public CommodityXRecyclerView(Context context, HashMap<String, String> map, CoordinatorLayout cl_root) {
        this(context, null, 0, map, cl_root);
    }

    public CommodityXRecyclerView(Context context, AttributeSet attrs, HashMap<String, String> map, CoordinatorLayout cl_root) {
        this(context, attrs, 0, map, cl_root);
    }

    public CommodityXRecyclerView(Context context, AttributeSet attrs, int defStyle, HashMap<String, String> map, CoordinatorLayout cl_root) {
        super(context, attrs, defStyle);
        this.context = context;
        this.map = map;
        this.cl_root = cl_root;

        checkWhereFrom();
        initView();
        initData();
    }

    private void checkWhereFrom() {
        mAll = map.get("all");
        mUserId = map.get("userId");
        mCategory = map.get("category");
        mQuery = map.get("query");
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        setLoadingMoreProgressStyle(ProgressStyle.TriangleSkewSpin);
        //设置Item增加、移除动画
        setItemAnimator(new DefaultItemAnimator());
        //设置刷新和加载监听
        setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mCommodityList.clear();
                        mCommodityList = null;
                        get10DataFromService("0");
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore() {
                if (mAdapter != null) {
                    get10DataFromService(String.valueOf(mCommodityList.size()));
                } else {
                    loadMoreComplete();
                }
            }
        });
    }

    private void initData() {
        get10DataFromService("0");
    }

    private void get10DataFromService(String index) {
        if (!isRefresh) {
            UIUtils.progressDialog(context);
        }
        final RequestParams params = new RequestParams(UsedMarketURL.SEARCH_COMMODITY);

        //根据whereFrom判断请求参数
        if (!TextUtils.isEmpty(mAll) && TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("type", "all");
        } else if (!TextUtils.isEmpty(mUserId) && TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("type", "t_commodity.user_id");
            params.addQueryStringParameter("queryValue", mUserId);
        } else if (!TextUtils.isEmpty(mCategory) && TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("type", "category");
            params.addQueryStringParameter("queryValue", mCategory);
        } else if (!TextUtils.isEmpty(mUserId) && !TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("type", "t_commodity.user_id");
            params.addQueryStringParameter("queryValue", mUserId);
            params.addQueryStringParameter("indistinctField", mQuery);
        } else if (!TextUtils.isEmpty(mCategory) && !TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("type", "category");
            params.addQueryStringParameter("queryValue", mCategory);
            params.addQueryStringParameter("indistinctField", mQuery);
        } else if (!TextUtils.isEmpty(mAll) && !TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("type", "all");
            params.addQueryStringParameter("indistinctField", mQuery);
        }

        params.addQueryStringParameter("index", index);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                Log.e("commodity",result);
                //刷新逻辑
                if (mCommodityList == null) {
                    mCommodityList = GsonUtils.getGson().fromJson(result, new TypeToken<List<Commodity>>() {}.getType());

                    //如果没有数据，则显示一个空白样式的图片
                    if (mCommodityList.size() < 1) {
                        EmptyAdapter emptyAdapter = new EmptyAdapter();
                        setAdapter(emptyAdapter);
                    } else {
                        Log.e("commodity",mCommodityList.get(0).toString());
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
                        //只有在用户发布界面才能有长按删除操作
                        if (!TextUtils.isEmpty(mUserId)) {
                            //设置条目长按监听
                            mAdapter.setOnItemLongClickListener(new CommodityXRecyclerViewAdapter.OnRecyclerViewItemLongClickListener() {
                                @Override
                                public void onItemLongClick(View view, Commodity commodity, final int position) {
                                    showPopupWindow(view, commodity, position);
                                }
                            });
                        }

                        setAdapter(mAdapter);
                    }
                    refreshComplete();
                }
                //加载更多逻辑
                else {
                    UIUtils.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<Commodity> newData = (List<Commodity>) GsonUtils.getGson().fromJson(result, new TypeToken<List<Commodity>>() {
                            }.getType());
                            if (newData.size() >= 1) {
                                mAdapter.add(newData);
                            } else {
                                UIUtils.snackBar(cl_root, "没有更多咯");
                            }
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
                RequestParams params1 = new RequestParams(UsedMarketURL.DELETE_COMMODITY);
                params1.addQueryStringParameter("commodityId", commodity.commodityId);
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
