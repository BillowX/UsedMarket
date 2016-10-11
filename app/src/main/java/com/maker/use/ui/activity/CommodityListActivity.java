package com.maker.use.ui.activity;

import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
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

    private MyXRecyclerView mMyXRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        HashMap<String, String> map = new HashMap<>();
        String username = getIntent().getStringExtra("username");
        String category = getIntent().getStringExtra("category");
        if (!TextUtils.isEmpty(username)) {
            map.put("username", username);
            fab_add.setVisibility(View.VISIBLE);
            //将发布按钮绘制成圆形(5.0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
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
            }
            //添加按钮点击事件
            fab_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivity(new Intent(UIUtils.getContext(), UploadCommodityActivity.class));

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

        } else if (!TextUtils.isEmpty(category)) {
            map.put("category", category);
            fab_add.setVisibility(View.GONE);
        }

        mMyXRecyclerView = new MyXRecyclerView(UIUtils.getContext(), map);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMyXRecyclerView.setLayoutParams(layoutParams);
        rl_root.addView(mMyXRecyclerView, 0, layoutParams);

    }

}
