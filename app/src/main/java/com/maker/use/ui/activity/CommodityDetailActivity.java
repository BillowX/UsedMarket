package com.maker.use.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
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

import com.bumptech.glide.Glide;
import com.maker.use.R;
import com.maker.use.domain.Commodity;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.adapter.GalleryAdapter;
import com.maker.use.ui.view.GalleryView;
import com.maker.use.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;

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
    @ViewInject(R.id.iv_userHeadImg)
    private ImageView iv_userHeadimg;
    @ViewInject(R.id.tv_userName)
    private TextView tv_userName;
    @ViewInject(R.id.tv_goods_price)
    private TextView tv_goods_price;
    @ViewInject(R.id.tv_goods_time)
    private TextView tv_goods_time;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
    }

    private void initData() {
        mCommodity = (Commodity) getIntent().getSerializableExtra("commodity");
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
        Glide.with(this).load(UsedMarketURL.server_heart + "//" + mCommodity.imgurl).centerCrop().into(iv_head);
//        x.image().bind(iv_head, UsedMarketURL.server_heart + "//" + mCommodity.imgurl);

        //初始化中心布局
        if (mCommodity != null) {
            //用户头像
            /*ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setRadius(DensityUtil.dip2px(radius))
                    .setIgnoreGif(false)
                    .setCrop(true)//是否对图片进行裁剪
                    .setFailureDrawableId(R.drawable.register_default_head)
                    .setLoadingDrawableId(R.drawable.register_default_head)
                    .build();
            x.image().bind(iv_userHeadimg, UsedMarketURL.server_heart + "/head/" + mCommodity.username + "_head.jpg", imageOptions);*/
            Glide.with(UIUtils.getContext()).load(UsedMarketURL.server_heart + "/head/" + mCommodity.username + "_head.jpg")
                    .centerCrop().into(iv_userHeadimg);


            tv_userName.setText(mCommodity.username);
            tv_goods_price.setText("¥ " + mCommodity.price);
//            tv_goods_time.setText(mCommodity.time);
            tv_goods_name.setText(mCommodity.name);
            tv_goods_description.setText(mCommodity.description);
//            x.image().bind(iv_img, UsedMarketURL.server_heart + "//" + commodity.imgurl);


            final ArrayList<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(R.drawable.a,
                    R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recView_goods_img.setLayoutManager(linearLayoutManager);
            GalleryAdapter mAdapter = new GalleryAdapter(this, mDatas);
            recView_goods_img.setAdapter(mAdapter);

            recView_goods_img.setOnItemScrollChangeListener(new GalleryView.OnItemScrollChangeListener() {
                @Override
                public void onChange(View view, int position) {
                    iv_img.setImageResource(mDatas.get(position));
                }
            });

            mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    iv_img.setImageResource(mDatas.get(position));
                }
            });
        }

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
                UIUtils.snackBar(rl_detail_toggle,"没有更多咯~");
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
}
