package com.maker.use.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.maker.use.R;
import com.maker.use.domain.Comment;
import com.maker.use.domain.Commodity;
import com.maker.use.global.ConstentValue;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.adapter.CommentListViewAdapter;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.GsonUtils;
import com.maker.use.utils.KeyBoardUtils;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.TimeUtil;
import com.maker.use.utils.UIUtils;
import com.maker.use.utils.map.Location;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;


/**
 * 商品详情页
 */
@ContentView(R.layout.activity_commoditydetail)
public class CommodityDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public List<Comment> mCommentList;
    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.iv_head)
    ImageView iv_head;
    @ViewInject(R.id.iv_arrow)
    ImageView iv_arrow;
    @ViewInject(R.id.rl_detail_toggle)
    RelativeLayout rl_detail_toggle;
    @ViewInject(R.id.tv_detail_author)
    TextView tv_detail_author;
    @ViewInject(R.id.lv_comment)
    ListView lv_comment;
    @ViewInject(R.id.nineGrid)
    NineGridView nineGrid;
    @ViewInject(R.id.refresh_root)
    SwipeRefreshLayout refresh_root;
    @ViewInject(R.id.tv_sent_reply)
    TextView tv_sent_reply;
    @ViewInject(R.id.et_reply)
    EditText et_reply;
    InputMethodManager imm;//键盘管理器
    @ViewInject(R.id.iv_empty)
    TextView iv_empty;
    @ViewInject(R.id.iv_userHeadImg)
    private ImageView iv_userHeadimg;
    @ViewInject(R.id.tv_userName)
    private TextView tv_userName;
    @ViewInject(R.id.tv_goods_time)
    private TextView tv_goods_time;
    @ViewInject(R.id.tv_location)
    private TextView tv_location;
    @ViewInject(R.id.tv_good_num)
    private TextView tv_good_num;
    @ViewInject(R.id.tv_goods_price)
    private TextView tv_goods_price;
    @ViewInject(R.id.tv_goods_name)
    private TextView tv_goods_name;
    @ViewInject(R.id.tv_goods_description)
    private TextView tv_goods_description;
    @ViewInject(R.id.tv_good_state)
    private TextView tv_good_state;
    private Commodity mCommodity;
    private LinearLayout.LayoutParams mParams;
    //是否展开详情
    private boolean isOpen = false;
    private List<String> mSplitImgUrl;
    private Location mLocation;
    private CommentListViewAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        mLocation = new Location(this);
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止位置服务
        mLocation.stopLocation();
    }

    private void initData() {
        mCommodity = (Commodity) getIntent().getSerializableExtra("commodity");
        mSplitImgUrl = mCommodity.getImages();
        mCommentList = new ArrayList<>();
    }

    private void initView() {
        // 初始化Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_userHeadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommodityDetailActivity.this, UserDetailActivity.class);
                intent.putExtra("userId", mCommodity.getUserId());
                CommodityDetailActivity.this.startActivity(intent);
            }
        });

        initCommodityView();

        // 放在消息队列中运行, 解决当只有三行描述时也是7行高度的bug
        tv_goods_description.post(new Runnable() {
            @Override
            public void run() {
                // 默认展示7行的高度
                int shortHeight = getShortHeight();
                mParams = (LinearLayout.LayoutParams) tv_goods_description.getLayoutParams();
                mParams.height = shortHeight;

                tv_goods_description.setLayoutParams(mParams);
            }
        });

        rl_detail_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        //设置刷新布局和留言布局
        refresh_root.setOnRefreshListener(this);
        refresh_root.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_dark);
        tv_sent_reply.setOnClickListener(this);

        //在服务器上获取改物品的评论列表
        getCommentDataFromServer();
    }

    /**
     * 初始化商品信息
     */
    private void initCommodityView() {
        GlideUtils.setImg(this, UsedMarketURL.server_heart + "//" + mSplitImgUrl.get(0).replace("_", ""), iv_head);

        //初始化中心布局
        if (mCommodity != null) {
            //发布者信息
            GlideUtils.setCircleImageViewImg(this, UsedMarketURL.HEAD + mCommodity.getHeadPortrait(), iv_userHeadimg);
            tv_userName.setText(mCommodity.getUsername());
            tv_goods_time.setText(TimeUtil.format(mCommodity.getLaunchDate()));
            //商品信息
            tv_goods_name.setText(mCommodity.getCommodityName());
            tv_good_num.setText(mCommodity.getAmount());
            tv_goods_price.setText("¥ " + mCommodity.getPrice());
            tv_location.setText(mCommodity.getLocation());
            tv_goods_description.setText(mCommodity.getDescription());
            tv_good_state.setText("0".equals(mCommodity.getStatus()) ? "在售" : "1".equals(mCommodity.getStatus()) ? "交易中" : "已售出");

            //设置商品列表宫格
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();  //获取到图片地址集合
            for (int i = 0; i < mSplitImgUrl.size(); i++) {
                //ImageInfo 是他的实体类,用于image的地址
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(UsedMarketURL.server_heart + "//" + mSplitImgUrl.get(i));
                info.setBigImageUrl(UsedMarketURL.server_heart + "//" + mSplitImgUrl.get(i).replace("_", ""));
                imageInfo.add(info);
            }
            nineGrid.setAdapter(new NineGridViewClickAdapter(this, imageInfo));
        }
    }

    /**
     * 初始化评论信息
     */
    private void initCommentView() {
        if (mCommentList.size() < 1) {
            //没有评论
            iv_empty.setVisibility(View.VISIBLE);
        } else {
            iv_empty.setVisibility(View.GONE);
            if (mAdapter == null) {
                lv_comment.addFooterView(View.inflate(this, R.layout.layout_nomore, null));
            }
            mAdapter = new CommentListViewAdapter(this, mCommentList, mCommodity.getUserId());
            lv_comment.setAdapter(mAdapter);
        }
    }

    /**
     * 根据商品ID在服务器上获取改物品的评论列表
     */
    private void getCommentDataFromServer() {
        RequestParams requestParams = new RequestParams(UsedMarketURL.SEARCH_COMMENT);
        requestParams.addBodyParameter("commodityId", mCommodity.getCommodityId());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mCommentList = GsonUtils.getGson().fromJson(result, new TypeToken<List<Comment>>() {
                }.getType());
                initCommentView();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                refresh_root.setRefreshing(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_commodity_detail_title, menu);
        return true;
    }


    /**
     * 展开或关闭详情
     */
    private void toggle() {
        int shortHeight = getShortHeight();
        int longHeight = getLongHeight();

        ValueAnimator animator = null;
        if (isOpen) {
            // 关闭
            isOpen = false;
            // 只有描述信息大于7行,才启动动画
            if (longHeight > shortHeight) {
                animator = ValueAnimator.ofInt(longHeight, shortHeight);
            }
        } else {
            // 打开
            isOpen = true;
            // 只有描述信息大于7行,才启动动画
            if (longHeight > shortHeight) {
                animator = ValueAnimator.ofInt(shortHeight, longHeight);
            } else {
                rl_detail_toggle.setVisibility(View.GONE);
                UIUtils.snackBar(rl_detail_toggle, "没有更多咯~");
            }
        }

        // 只有描述信息大于7行,才启动动画
        if (animator != null) {
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator arg0) {
                    Integer height = (Integer) arg0.getAnimatedValue();
                    mParams.height = height;
                    tv_goods_description.setLayoutParams(mParams);
                }

            });
            //设置动画监听
            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator arg0) {

                }

                @Override
                public void onAnimationRepeat(Animator arg0) {

                }

                @Override
                public void onAnimationEnd(Animator arg0) {
                    // ScrollView要滑动到最底部
                    final NestedScrollView scrollView = getScrollView();

                    // 为了运行更加安全和稳定, 可以讲滑动到底部方法放在消息队列中执行
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            // scrollView滚动到底部
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                    if (isOpen) {
                        tv_detail_author.setText("点击收缩详情");
                        iv_arrow.setImageResource(R.drawable.arrow_up);
                    } else {
                        tv_detail_author.setText("点击展开详情");
                        iv_arrow.setImageResource(R.drawable.arrow_down);
                    }

                }

                @Override
                public void onAnimationCancel(Animator arg0) {

                }
            });

            animator.setDuration(200);
            animator.start();
        }
    }

    // 获取ScrollView, 一层一层往上找,
    // 知道找到ScrollView后才返回;注意:一定要保证父控件或祖宗控件有ScrollView,否则死循环
    private NestedScrollView getScrollView() {
        ViewParent parent = tv_goods_description.getParent();

        while (!(parent instanceof NestedScrollView)) {
            parent = parent.getParent();
        }

        return (NestedScrollView) parent;
    }

    /**
     * 模拟一个textview,设置最大行数为7行
     * 计算该虚拟textview的高度, 从而知道tvDes在展示7行时应该多高
     *
     * @return
     */
    private int getShortHeight() {
        int width = tv_goods_description.getMeasuredWidth();

        //模拟textview
        TextView textView = new TextView(UIUtils.getContext());
        //设置文本一致，文字大小一致
        textView.setText(mCommodity.getDescription());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        //设置最多为7行
        textView.setMaxLines(7);

        // 宽不变, 确定值, match_parent
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        // 高度包裹内容, wrap_content;当包裹内容时,参1表示尺寸最大值,暂写2000, 也可以是屏幕高度
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(20000, View.MeasureSpec.AT_MOST);
        //开始测量
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        //返回测量后的高度
        return textView.getMeasuredHeight();
    }

    /**
     * 模拟一个textview,
     * 计算该虚拟textview的高度, 从而知道tvDes在完全展示时应该多高
     *
     * @return
     */
    private int getLongHeight() {
        int width = tv_goods_description.getMeasuredWidth();

        //模拟textview
        TextView textView = new TextView(UIUtils.getContext());
        //设置文本一致，文字大小一致
        textView.setText(mCommodity.getDescription());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        // 宽不变, 确定值, match_parent
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        // 高度包裹内容, wrap_content;当包裹内容时,参1表示尺寸最大值,暂写2000, 也可以是屏幕高度
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(20000, View.MeasureSpec.AT_MOST);
        //开始测量
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        //返回测量后的高度
        return textView.getMeasuredHeight();
    }

    //联系卖家按钮触发
    public void ContactSeller(View view) {
        if (SpUtil.getBoolean(ConstentValue.IS_LOGIN, false)) {
            //启动会话界面
            if (RongIM.getInstance() != null)
                RongIM.getInstance().startPrivateChat(this, mCommodity.getUserId(), mCommodity.getUsername());
        } else {
            startActivity(new Intent(UIUtils.getContext(), LoginActivity.class));
        }

    }

    @Override
    public void onRefresh() {
        //根据商品ID去请求最新的商品信息
        //....
        refreshCommodity();
    }

    /**
     * 刷新商品信息
     */
    private void refreshCommodity() {
        final RequestParams params = new RequestParams(UsedMarketURL.SEARCH_COMMODITY);
        params.addBodyParameter("type", "commodity_id");
        params.addBodyParameter("queryValue", mCommodity.getCommodityId());
        params.addBodyParameter("index", "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
//                Log.e("mCommodityList",result);
                List<Commodity> commodityList= GsonUtils.getGson().fromJson(result, new TypeToken<List<Commodity>>() {
                }.getType());
                mCommodity = commodityList.get(0);
                initCommodityView();
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
                refresh_root.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sent_reply:
                if (SpUtil.getBoolean(ConstentValue.IS_LOGIN, false)) {
                    sentReply();
                } else {
                    UIUtils.toast("请先登录");
                    startActivity(new Intent(UIUtils.getContext(), LoginActivity.class));
                }
                break;
        }
    }

    /**
     * 发布评论
     */
    private void sentReply() {
        String center = et_reply.getText().toString();
        if (TextUtils.isEmpty(center)) {
            UIUtils.toast("评论还没写呢");
            return;
        }
        tv_sent_reply.setEnabled(false);
        Comment comment = new Comment();
        comment.setCommentText(center);//内容
        comment.setCommodityId(mCommodity.getCommodityId());//商品id
        comment.setUserId(SpUtil.getUserId());//userId
        comment.setCommentLocation(mLocation.city);//位置信息
        upComment(comment);
    }

    /**
     * 上传评论到服务器
     *
     * @param comment
     */
    private void upComment(Comment comment) {
        UIUtils.progressDialog(this);
        RequestParams requestParams = new RequestParams(UsedMarketURL.INSERT_COMMENT);
        requestParams.addBodyParameter("commodityId", comment.getCommodityId());
        requestParams.addBodyParameter("commentText", comment.getCommentText());
        requestParams.addBodyParameter("userId", comment.getUserId());
        requestParams.addBodyParameter("commentLocation", comment.getCommentLocation());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("comment", result);
                UIUtils.toast("评论成功");
                et_reply.setText("");
                if (imm.isActive()) {//关闭键盘
                    KeyBoardUtils.closeSoftKeyboard(CommodityDetailActivity.this);
                }
                getCommentDataFromServer();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                UIUtils.closeProgressDialog();
                tv_sent_reply.setEnabled(true);
            }
        });
    }
}
