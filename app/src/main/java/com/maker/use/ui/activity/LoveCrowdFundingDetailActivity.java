package com.maker.use.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.maker.use.R;
import com.maker.use.ui.adapter.FragmentViewPagerAdapter;
import com.maker.use.ui.fragment.loveCrowdFundingFragment.CommentFragment;
import com.maker.use.ui.fragment.loveCrowdFundingFragment.HomepageFragment;
import com.maker.use.ui.fragment.loveCrowdFundingFragment.SupporterFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 创意众筹详情页
 * Created by XT on 2016/10/22.
 */
@ContentView(R.layout.activity_love_crow_funding_detail)
public class LoveCrowdFundingDetailActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化viewpager
        if (vp_crowd_funding != null) {
            FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(this.getSupportFragmentManager());
            adapter.addFragment(new HomepageFragment(), "主页");
            adapter.addFragment(new CommentFragment(), "留言评论");
            adapter.addFragment(new SupporterFragment(), "支持者");
            vp_crowd_funding.setAdapter(adapter);
        }
        //将TabLayout与ViewPager绑定
        tb_originality.setupWithViewPager(vp_crowd_funding);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }
}
