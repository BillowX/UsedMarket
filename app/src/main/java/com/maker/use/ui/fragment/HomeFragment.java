package com.maker.use.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.maker.use.R;
import com.maker.use.domain.User;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.activity.MainActivity;
import com.maker.use.ui.activity.SearchActivity;
import com.maker.use.ui.adapter.FragmentViewPagerAdapter;
import com.maker.use.ui.fragment.homeFragment.ShopFragment;
import com.maker.use.ui.fragment.homeFragment.UsedFragment;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.LoginUtils;
import com.maker.use.utils.UIUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 主页界面
 * Created by XT on 2016/9/28.
 */

public class HomeFragment extends BaseFragment {

    @ViewInject(R.id.vp_home)
    ViewPager vp_home;
    @ViewInject(R.id.tl_home)
    TabLayout tl_home;
    @ViewInject(R.id.ll_find)
    private LinearLayout ll_find;
    @ViewInject(R.id.iv_head)
    private CircleImageView iv_head;
    @ViewInject(R.id.iv_setting)
    private ImageView iv_setting;

    private MainActivity mActivity;
    private View mMainView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_home, null);
        x.view().inject(this, mMainView);

        initView();

        return mMainView;
    }

    public void initView() {
        mActivity = (MainActivity) getActivity();
        //设置登陆状态监听，来设置头像图片
        LoginUtils.setOnLoginStatusChangeListener(new LoginUtils.onLoginStatusChangeListener() {
            @Override
            public void onLogin(User user) {
                //头像
                GlideUtils.setCircleImageViewImg(mActivity, UsedMarketURL.HEAD + user.getHeadPortrait(), iv_head);
            }
        });

        //给toolbar上的两个侧边栏开关按钮设置监听
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.getDrawerLayout().openDrawer(Gravity.LEFT);
            }
        });
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.getDrawerLayout().openDrawer(Gravity.RIGHT);
            }
        });
        //给查找按钮设置监听
        ll_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UIUtils.getContext(), SearchActivity.class);
                intent.putExtra("all", "all");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mActivity).toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
        //初始化viewpager
        if (vp_home != null) {
            FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(mActivity.getSupportFragmentManager());
            adapter.addFragment(new UsedFragment(), "二手");
            adapter.addFragment(new ShopFragment(), "商铺");
            vp_home.setAdapter(adapter);
        }
        //将TabLayout与ViewPager绑定
        tl_home.setupWithViewPager(vp_home);
    }

    @Override
    public void onStop() {
        super.onStop();
//        pagerHeader.setAutoLoop(false, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
//        pagerHeader.setAutoLoop(true, 3000);
    }

}