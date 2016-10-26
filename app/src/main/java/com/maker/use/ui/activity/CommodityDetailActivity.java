package com.maker.use.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.domain.Commodity;
import com.maker.use.global.ConstentValue;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.adapter.CommentAdapter;
import com.maker.use.ui.adapter.GalleryAdapter;
import com.maker.use.ui.view.DividerLine;
import com.maker.use.ui.view.GalleryView;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.TimeUtil;
import com.maker.use.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import io.rong.imkit.RongIM;

/**
 * 商品详情页
 * Created by XISEVEN on 2016/10/9.
 */
@ContentView(R.layout.activity_commoditydetail)
public class CommodityDetailActivity extends BaseActivity {
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
    int index = 0;
    @ViewInject(R.id.ll_message_list)
    LinearLayout ll_message_list;
    @ViewInject(R.id.iv_userHeadImg)
    private ImageView iv_userHeadimg;
    @ViewInject(R.id.tv_userName)
    private TextView tv_userName;
    @ViewInject(R.id.tv_goods_time)
    private TextView tv_goods_time;
    @ViewInject(R.id.tv_good_num)
    private TextView tv_good_num;
    @ViewInject(R.id.tv_goods_price)
    private TextView tv_goods_price;
    @ViewInject(R.id.tv_goods_name)
    private TextView tv_goods_name;
    @ViewInject(R.id.iv_img)
    private ImageView iv_img;
    @ViewInject(R.id.recView_goods_img)
    private GalleryView recView_goods_img;
    @ViewInject(R.id.tv_goods_description)
    private TextView tv_goods_description;
    private Commodity mCommodity;
    private LinearLayout.LayoutParams mParams;
    //是否展开详情
    private boolean isOpen = false;
    private String[] mSplitImgUrl;
    private String[] mNewImgUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
    }

    private void initData() {
        mCommodity = (Commodity) getIntent().getSerializableExtra("commodity");
        mSplitImgUrl = mCommodity.images.split(";");
        mNewImgUrl = new String[mSplitImgUrl.length];
        for (int i = 0; i < mSplitImgUrl.length; i++) {
            mNewImgUrl[i] = mSplitImgUrl[mSplitImgUrl.length - i - 1];
        }

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

        GlideUtils.setImg(this, UsedMarketURL.server_heart + "//" + mSplitImgUrl[0].replace("_", ""), iv_head);
       /* Glide.with(this).load(UsedMarketURL.server_heart + "//" + mSplitImgUrl[0].replace("_","")).centerCrop().into(iv_head);*/

        //初始化中心布局
        if (mCommodity != null) {
            //发布者信息
            /*Glide.with(UIUtils.getContext()).load(UsedMarketURL.HEAD + mCommodity.headPortrait)
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(iv_userHeadimg);*/
            GlideUtils.setCircleImageViewImg(this, UsedMarketURL.HEAD + mCommodity.headPortrait, iv_userHeadimg);
            tv_userName.setText(mCommodity.username);
            tv_goods_time.setText(TimeUtil.format(mCommodity.launchDate));
            //商品信息
            tv_goods_name.setText(mCommodity.commodityName);
            tv_good_num.setText(mCommodity.amount);
            tv_goods_price.setText("¥ " + mCommodity.price);


            tv_goods_description.setText(mCommodity.description);
//            x.image().bind(iv_img, UsedMarketURL.server_heart + "//" + commodity.imgurl);

            //留言区
            RecyclerView recyclerView = new RecyclerView(UIUtils.getContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            //如果没有留言，则显示EmptyAdapter
//            recyclerView.setAdapter(new EmptyAdapter());
            recyclerView.setAdapter(new CommentAdapter(UIUtils.getContext()));
            //设置条目之间的分割线
            DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
            dividerLine.setSize(1);
            dividerLine.setColor(0xFFDDDDDD);
            recyclerView.addItemDecoration(dividerLine);
            ll_message_list.addView(recyclerView);

            //设置画廊效果
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recView_goods_img.setLayoutManager(linearLayoutManager);
            GalleryAdapter mAdapter = new GalleryAdapter(this, mNewImgUrl);
            recView_goods_img.setAdapter(mAdapter);

            recView_goods_img.setOnItemScrollChangeListener(new GalleryView.OnItemScrollChangeListener() {
                @Override
                public void onChange(View view, int position) {
                    /*ImageOptions imageOptions = new ImageOptions.Builder()
                            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                            .setIgnoreGif(false)
                            .setFailureDrawableId(R.drawable.error)
                            .setLoadingDrawableId(R.drawable.loading)
                            .build();
                    x.image().bind(iv_img, UsedMarketURL.server_heart + "//" + mNewImgUrl[position], imageOptions);*/
                    /*Glide.with(CommodityDetailActivity.this).load(UsedMarketURL.server_heart + "//" + mNewImgUrl[position].replace("_",""))
                            .centerCrop()
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.error)
                            .into(iv_img);*/
                    GlideUtils.setImg(CommodityDetailActivity.this, UsedMarketURL.server_heart + "//" + mNewImgUrl[position].replace("_", ""), iv_img);
                    index = position;
                }
            });

            mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    /*ImageOptions imageOptions = new ImageOptions.Builder()
                            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                            .setIgnoreGif(false)
                            .setFailureDrawableId(R.drawable.error)
                            .setLoadingDrawableId(R.drawable.loading)
                            .build();
                    x.image().bind(iv_img, UsedMarketURL.server_heart + "//" + mNewImgUrl[position].replace("_",""), imageOptions);*/
                    /*Glide.with(CommodityDetailActivity.this).load(UsedMarketURL.server_heart + "//" + mNewImgUrl[position].replace("_", ""))
                            .centerCrop()
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.error)
                            .into(iv_img);*/
                    GlideUtils.setImg(CommodityDetailActivity.this, UsedMarketURL.server_heart + "//" + mNewImgUrl[position].replace("_", ""), iv_img);
                    index = position;
                }
            });
        }

        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UIUtils.getContext(), ImgActivity.class);
                //开启一个新的栈
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra("index", index);
                intent.putExtra("imgUrl", mNewImgUrl);
                UIUtils.getContext().startActivity(intent);
                /*new ImageViewer.Builder(CommodityDetailActivity.this, mNewImgUrl)
                        .setStartPosition(index)
                        .show();*/
            }
        });

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
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
        textView.setText(mCommodity.description);
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
        textView.setText(mCommodity.description);
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
                RongIM.getInstance().startPrivateChat(this, mCommodity.userId, mCommodity.username);
        } else {
            startActivity(new Intent(UIUtils.getContext(), LoginActivity.class));
        }

    }
}
