package com.maker.use.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.maker.use.R;
import com.maker.use.ui.adapter.FragmentViewPagerAdapter;
import com.maker.use.ui.fragment.originalityCrowdFundingDetailFragment.HomepageFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by XT on 2016/10/22.
 */
@ContentView(R.layout.activity_originality_crowd_funding_detail)
public class OriginalityCrowdFundingDetailActivity extends BaseActivity {

    @ViewInject(R.id.vp_crowd_funding)
    ViewPager vp_crowd_funding;
    @ViewInject(R.id.tb_originality)
    TabLayout tb_originality;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
//初始化viewpager
        if (vp_crowd_funding != null) {
            FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(this.getSupportFragmentManager());
            adapter.addFragment(new HomepageFragment(), "作品主页");
            adapter.addFragment(new HomepageFragment(), "留言评论");
            adapter.addFragment(new HomepageFragment(), "支持者");
            vp_crowd_funding.setAdapter(adapter);
        }
        //将TabLayout与ViewPager绑定
        tb_originality.setupWithViewPager(vp_crowd_funding);
    }
}
