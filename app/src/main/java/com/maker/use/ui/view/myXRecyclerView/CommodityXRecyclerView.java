package com.maker.use.ui.view.myXRecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
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

    private List<Commodity> mCommodityList;
    private CommodityXRecyclerViewAdapter mAdapter;
    private String type;//查询类型 all，t_commodity.user_id，category
    private String queryValue;
    private String indistinctField;
    private String order;
    private String orderBy;
    private PopupWindow mPopupWindow;
    //判断是不是刷新逻辑，如果不是，说明第一次进入，那么显示加载中对话框,如果不是第一次进入时的请求数据操作，则不显示加载进度条
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
        type = map.get("type");
        queryValue = map.get("queryValue");
        indistinctField = map.get("indistinctField");
        order = map.get("order");
        orderBy = map.get("orderBy");
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        setLoadingMoreProgressStyle(ProgressStyle.TriangleSkewSpin);
        //设置Item增加、移除动画
//        setItemAnimator(new DefaultItemAnimator());
        //设置刷新和加载监听
        setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mCommodityList = null;
                get10DataFromService("0");
            }

            @Override
            public void onLoadMore() {
                get10DataFromService(String.valueOf(mCommodityList.size()));
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
        params.addBodyParameter("type", type);
        if (!TextUtils.isEmpty(queryValue)) {
            params.addBodyParameter("queryValue", queryValue);
        }
        if (!TextUtils.isEmpty(indistinctField)) {
            params.addBodyParameter("indistinctField", indistinctField);
        }
        params.addBodyParameter("order", order);
        params.addBodyParameter("orderBy", orderBy);
        params.addBodyParameter("index", index);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
//                Log.e("mCommodityList",result);
                //刷新逻辑
                if (mCommodityList == null) {
                    //开启加载更多功能
                    setLoadingMoreEnabled(true);
                    mCommodityList = GsonUtils.getGson().fromJson(result, new TypeToken<List<Commodity>>() {
                    }.getType());

                    //如果没有数据，则显示一个空白样式的图片
                    if (mCommodityList.size() < 1) {
                        EmptyAdapter emptyAdapter = new EmptyAdapter();
                        setAdapter(emptyAdapter);
                        setLoadingMoreEnabled(false);
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
                        //只有在用户发布界面才能有长按操作
                        if ("t_commodity.user_id".equals(type)) {
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
                    List<Commodity> newData = (List<Commodity>) GsonUtils.getGson().fromJson(result, new TypeToken<List<Commodity>>() {
                    }.getType());
                    if (newData.size() >= 1) {
                        mAdapter.add(newData);
                    } else {
                        UIUtils.snackBar(cl_root, "没有更多咯");
                        //禁用加载更多
                        setLoadingMoreEnabled(false);
                    }
                    loadMoreComplete();
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
        Button bt_alter_num = (Button) popupWindowView
                .findViewById(R.id.bt_alter_num);
        Button bt_alter_price = (Button) popupWindowView
                .findViewById(R.id.bt_alter_price);
        Button bt_alter_status = (Button) popupWindowView
                .findViewById(R.id.bt_alter_status);

        bt_alter_num.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                showChangeNumDialog(commodity);
            }
        });
        bt_alter_status.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                showChangeStatusDialog(commodity);
            }
        });
        bt_alter_price.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePriceDialog(commodity);
            }
        });
        bt_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                deleteCommodity(commodity, position);
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
    }

    /**
     * @param commodity
     */
    private void showChangeStatusDialog(final Commodity commodity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("选择商品状态");
        builder.setIcon(R.mipmap.logo);
        String items[] = new String[]{"在售", "交易中", "已售出"};
        final int[] chooseItem = {Integer.parseInt(commodity.getStatus())};
        builder.setSingleChoiceItems(items, chooseItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chooseItem[0] = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                UIUtils.progressDialog(context);
                RequestParams params1 = new RequestParams(UsedMarketURL.UPDATA_COMMODITY);
                params1.addQueryStringParameter("commodityId", commodity.getCommodityId());
                params1.addQueryStringParameter("status", chooseItem[0] + "");
                x.http().get(params1, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if ("更新成功".equals(result)) {
                            UIUtils.toast("修改成功");
                            dialog.dismiss();
                            mCommodityList = null;
                            get10DataFromService("0");
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
                        UIUtils.closeProgressDialog();
                    }
                });
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 显示修改商品价格的对话框
     */
    private void showChangePriceDialog(final Commodity commodity) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        final View view = View.inflate(context, R.layout.dialog_change_commodity_price, null);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        final Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_commodity_price = (EditText) view.findViewById(R.id.et_commodity_price);
                String price = et_commodity_price.getText().toString();

                if (TextUtils.isEmpty(price)) {
                    UIUtils.toast("内容不能为空");
                } else if ("0".equals(price) || "00".equals(price) || "000".equals(price) || "0000".equals(price) || "00000".equals(price)) {
                    UIUtils.toast("价格不能为0");
                } else {
                    UIUtils.progressDialog(context);
                    RequestParams params1 = new RequestParams(UsedMarketURL.UPDATA_COMMODITY);
                    params1.addQueryStringParameter("commodityId", commodity.getCommodityId());
                    params1.addQueryStringParameter("price", price);
                    x.http().get(params1, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if ("更新成功".equals(result)) {
                                UIUtils.toast("修改成功");
                                dialog.dismiss();
                                mCommodityList = null;
                                get10DataFromService("0");
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
                            UIUtils.closeProgressDialog();
                        }
                    });
                }

            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示修改商品数量的对话框
     */
    private void showChangeNumDialog(final Commodity commodity) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        final View view = View.inflate(context, R.layout.dialog_change_commodity_num, null);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        final Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_commodity_num = (EditText) view.findViewById(R.id.et_commodity_num);
                String num = et_commodity_num.getText().toString();

                if (TextUtils.isEmpty(num)) {
                    UIUtils.toast("内容不能为空");
                } else if ("0".equals(num) || "00".equals(num)) {
                    UIUtils.toast("数量不能为0");
                } else {
                    UIUtils.progressDialog(context);
                    RequestParams params1 = new RequestParams(UsedMarketURL.UPDATA_COMMODITY);
                    params1.addQueryStringParameter("commodityId", commodity.getCommodityId());
                    params1.addQueryStringParameter("amount", num);
                    x.http().get(params1, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if ("更新成功".equals(result)) {
                                UIUtils.toast("修改成功");
                                dialog.dismiss();
                                mCommodityList = null;
                                get10DataFromService("0");
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
                            UIUtils.closeProgressDialog();
                        }
                    });
                }

            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 在服务器上删除商品
     *
     * @param commodity
     * @param position
     */
    private void deleteCommodity(Commodity commodity, final int position) {
        UIUtils.progressDialog(context);
        RequestParams params1 = new RequestParams(UsedMarketURL.DELETE_COMMODITY);
        params1.addQueryStringParameter("commodityId", commodity.getCommodityId());
        x.http().get(params1, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if ("删除成功".equals(result)) {
                    if (mCommodityList != null) {
                        mCommodityList.remove(position);
                        if (mCommodityList.size() < 1) {
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
    }

    @Override
    public void onClick(View v) {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

}
