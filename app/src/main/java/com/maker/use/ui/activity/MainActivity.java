package com.maker.use.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.maker.use.R;
import com.maker.use.domain.User;
import com.maker.use.global.ConstentValue;
import com.maker.use.ui.fragment.ClassifyFragment;
import com.maker.use.ui.fragment.DynamicFragment;
import com.maker.use.ui.fragment.HomeFragment;
import com.maker.use.ui.fragment.MessageFragment;
import com.maker.use.ui.view.MainNavigateTabBar;
import com.maker.use.utils.GsonUtils;
import com.maker.use.utils.LoginUtils;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.UIUtils;
import com.nineoldandroids.view.ViewHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.nv_right)
    NavigationView nv_right;
    @ViewInject(R.id.dl_root)
    private DrawerLayout dl_root;
    @ViewInject(R.id.mainTabBar)
    private MainNavigateTabBar mNavigateTabBar;

    private onFragmentChangeListener mOnFragmentChangeListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigateTabBar.onRestoreInstanceState(savedInstanceState);

        initView();
        initUserData();
    }

    private void initUserData() {
        //在刚打开应用时，检查是否登陆过，如果有的话提取保存的用户信息进行登陆验证
        if (!SpUtil.getBoolean(ConstentValue.IS_LOGIN, false)) {
            String s = SpUtil.getString(ConstentValue.USER, "");
            if (!TextUtils.isEmpty(s)) {
                User user = GsonUtils.getGson().fromJson(s, User.class);
                LoginUtils.login(user.getUsername(), user.getPassword(), this);
            }
        }

        //在登陆页面登陆后返回的登陆操作
        if ("login".equals(getIntent().getStringExtra("info"))) {
            /*dl_root.openDrawer(Gravity.LEFT);
            dl_root.invalidate();*/
        }

    }

    private void initView() {
        String[] tabTags = UIUtils.getStringArray(R.array.tab_tag);
        mNavigateTabBar.addTab(HomeFragment.class, new MainNavigateTabBar.TabParam(R.drawable.main_home_normal, R.drawable.main_home_selected, tabTags[0]));
        mNavigateTabBar.addTab(ClassifyFragment.class, new MainNavigateTabBar.TabParam(R.drawable.main_classify_normal, R.drawable.main_classify_selected, tabTags[1]));
        mNavigateTabBar.addTab(null, new MainNavigateTabBar.TabParam(0, 0, tabTags[2]));
        mNavigateTabBar.addTab(DynamicFragment.class, new MainNavigateTabBar.TabParam(R.drawable.main_dynamic_normal, R.drawable.main_dynamic_selected, tabTags[3]));
        mNavigateTabBar.addTab(MessageFragment.class, new MainNavigateTabBar.TabParam(R.drawable.main_message_normal, R.drawable.main_message_selected, tabTags[4]));
//        mNavigateTabBar.setCurrentSelectedTab(3);
        mNavigateTabBar.setTabSelectListener(new MainNavigateTabBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(MainNavigateTabBar.ViewHolder holder) {
                if (!"HomeFragment".equals(holder.fragmentClass.getSimpleName())) {
                    mOnFragmentChangeListener.onFragmentChange();
                } else {
                    mOnFragmentChangeListener.onFragmentIsHomeFragment();
                }
            }
        });

        //侧边栏监听
        dl_root.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //slideOffset-滑动（0-1）
                View mContent = dl_root.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;
                if (drawerView.getTag().equals("LEFT")) {

                    float leftScale = 1 - 0.3f * scale;

                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent,
                            mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);

                    Log.e("test", "leftScale:" + leftScale + ",scale:" + scale + ",rightScale" + rightScale);

                } else if (drawerView.getTag().equals("RIGHT")) {
                    ViewHelper.setTranslationX(mContent,
                            -mMenu.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }
        });

        //显示图标颜色
        nv_right.setItemIconTintList(null);
        nv_right.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.wifi:
//                        item.setIcon()
                        break;
                }
                Snackbar.make(nv_right, "敬请期待", Snackbar.LENGTH_LONG)
                        .setAction("知道啦", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //
                            }
                        }).setActionTextColor(Color.BLUE)
                        .show();
                return false;
            }
        });

    }

    //保存状态
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigateTabBar.onSaveInstanceState(outState);
    }

    /**
     * 获取DrawerLayout去关闭打开侧边栏
     */
    public DrawerLayout getDrawerLayout() {
        return dl_root;
    }

    //发布按钮触发
    public void issue(View view) {
        startActivity(new Intent(UIUtils.getContext(), IssueActivity.class));
    }

    @Override
    public void onBackPressed() {
        SpUtil.putBoolean(ConstentValue.IS_LOGIN, false);
        super.onBackPressed();
    }

    public void setOnFragmentChangeListener(onFragmentChangeListener listener) {
        this.mOnFragmentChangeListener = listener;
    }

    public interface onFragmentChangeListener {
        public void onFragmentChange();

        public void onFragmentIsHomeFragment();
    }
}
