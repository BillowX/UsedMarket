package com.maker.use.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.maker.use.ui.adapter.MyXRecyclerViewAdapter;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;


/**
 * 自定义的XRecyclerView
 * Created by XT on 2016/10/9.
 */

public class MyXRecyclerView extends XRecyclerView implements View.OnClickListener {

    private HashMap<String, String> map;

    //刷新时间
    private int refreshTime = 0;
    private List<Commodity> mCommoditys;
    private MyXRecyclerViewAdapter mAdapter;
    private String mAll;
    private String mUsername;
    private String mCategory;
    private PopupWindow mPopupWindow;
    private String mQuery;

    public MyXRecyclerView(Context context, HashMap<String, String> map) {
        this(context, null, 0, map);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs, HashMap<String, String> map) {
        this(context, attrs, 0, map);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs, int defStyle, HashMap<String, String> map) {
        super(context, attrs, defStyle);
        this.map = map;

        checkWhereFrom();
        initView();
        initData();
    }

    private void checkWhereFrom() {
        mAll = map.get("all");
        mUsername = map.get("username");
        mCategory = map.get("category");
        mQuery = map.get("query");
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        setLoadingMoreProgressStyle(ProgressStyle.Pacman);
//      setArrowImageView(R.drawable.iconfont_downgrey);
        //设置Item增加、移除动画
        setItemAnimator(new DefaultItemAnimator());
        //设置条目之间的分割线
//        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
//        dividerLine.setSize(1);
//        dividerLine.setColor(0xFFDDDDDD);
//        addItemDecoration(dividerLine);
        //设置刷新和加载监听
        setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mCommoditys.clear();
                        mCommoditys = null;
                        get10CommoditysFromService("0");
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore() {
                if (mAdapter != null) {
                    get10CommoditysFromService(String.valueOf(mCommoditys.size()));
                } else {
                    loadMoreComplete();
                }
            }
        });
    }

    private void initData() {
        get10CommoditysFromService("0");
    }

    private void get10CommoditysFromService(String index) {
        final RequestParams params = new RequestParams(UsedMarketURL.server_heart + "/servlet/FindCommodityServlet");

        //根据whereFrom判断请求参数
        if (!TextUtils.isEmpty(mAll) && TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("all", mAll);
        } else if (!TextUtils.isEmpty(mUsername) && TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("username", mUsername);
        } else if (!TextUtils.isEmpty(mCategory) && TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("category", mCategory);
        } else if (!TextUtils.isEmpty(mUsername) && !TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("username", mUsername);
            params.addQueryStringParameter("query", mQuery);
        } else if (!TextUtils.isEmpty(mCategory) && !TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("category", mCategory);
            params.addQueryStringParameter("query", mQuery);
        }else if (!TextUtils.isEmpty(mAll) && !TextUtils.isEmpty(mQuery)) {
            params.addQueryStringParameter("all", mAll);
            params.addQueryStringParameter("query", mQuery);
        }

        params.addQueryStringParameter("index", index);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                final Gson gson = new Gson();
                //刷新逻辑
                if (mCommoditys == null) {
                    mCommoditys = gson.fromJson(result, new TypeToken<List<Commodity>>() {
                    }.getType());

                    //如果没有数据，则显示一个空白样式的图片
                    if (mCommoditys.size() < 1) {
//                        UIUtils.toast("暂无数据哦");
                        EmptyAdapter emptyAdapter = new EmptyAdapter();
                        setAdapter(emptyAdapter);
                    } else {
                        //设置适配器
                        mAdapter = new MyXRecyclerViewAdapter(mCommoditys);
                        //设置条目点击监听
                        mAdapter.setOnItemClickListener(new MyXRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, Commodity commodity) {
                                Intent intent = new Intent(UIUtils.getContext(), CommodityDetailActivity.class);
                                intent.putExtra("commodity", commodity);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                UIUtils.getContext().startActivity(intent);

                            }
                        });
                        //只有在用户发布界面才能有长按删除操作
                        if (!TextUtils.isEmpty(mUsername)) {
                            //设置条目长按监听
                            mAdapter.setOnItemLongClickListener(new MyXRecyclerViewAdapter.OnRecyclerViewItemLongClickListener() {
                                @Override
                                public void onItemLongClick(View view, Commodity commodity, final int position) {
                                    showPopupWindow(view, commodity, position);
                                }
                            });
                        }

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
//                                mCommoditys.addAll(newData);
                            } else {
                                UIUtils.toast("没有更多咯");
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

            }
        });
    }

    public MyXRecyclerViewAdapter getMyXRecyclerViewAdapter() {
        return mAdapter;
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
                RequestParams params1 = new RequestParams(UsedMarketURL.server_heart + "/servlet/DeleteCommodityServlet");
                params1.addQueryStringParameter("id", String.valueOf(commodity.id));
                x.http().get(params1, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if ("删除成功".equals(result)) {
                            if (mCommoditys != null) {
                                mCommoditys.remove(position);
                                if (mCommoditys.size() < 1) {
                                    mCommoditys.clear();
                                    mCommoditys = null;
                                    get10CommoditysFromService("0");
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

                    }
                });
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
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

    /**
     * 数据为空时的RecyclerView的条目适配器
     */
    class EmptyAdapter extends RecyclerView.Adapter<EmptyAdapter.MyEmptyViewHolder> {

        @Override
        public MyEmptyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_commoditylist_empty, parent, false);
            MyEmptyViewHolder holder = new MyEmptyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyEmptyViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        class MyEmptyViewHolder extends RecyclerView.ViewHolder {
            public MyEmptyViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 分隔线装饰
     */
    class DividerLine extends RecyclerView.ItemDecoration {
        /**
         * 水平方向
         */
        public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

        /**
         * 垂直方向
         */
        public static final int VERTICAL = LinearLayoutManager.VERTICAL;

        // 画笔
        private Paint paint;

        // 布局方向
        private int orientation;
        // 分割线颜色
        private int color;
        // 分割线尺寸
        private int size;

        public DividerLine() {
            this(VERTICAL);
        }

        public DividerLine(int orientation) {
            this.orientation = orientation;

            paint = new Paint();
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            if (orientation == VERTICAL) {
                drawHorizontal(c, parent);
            } else {
                drawVertical(c, parent);
            }
        }

        /**
         * 设置分割线颜色
         *
         * @param color 颜色
         */
        public void setColor(int color) {
            this.color = color;
            paint.setColor(color);
        }

        /**
         * 设置分割线尺寸
         *
         * @param size 尺寸
         */
        public void setSize(int size) {
            this.size = size;
        }

        // 绘制垂直分割线
        protected void drawVertical(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + size;

                c.drawRect(left, top, right, bottom, paint);
            }
        }

        // 绘制水平分割线
        protected void drawHorizontal(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + size;

                c.drawRect(left, top, right, bottom, paint);
            }
        }
    }

}
