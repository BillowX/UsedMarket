package com.maker.use.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maker.use.R;
import com.maker.use.ui.activity.MainActivity;
import com.maker.use.ui.fragment.donateFragment.CampusDynamicFragment;
import com.maker.use.ui.fragment.donateFragment.DonationsDynamicFragment;
import com.maker.use.ui.fragment.donateFragment.LoveCrowdFundingFragment;
import com.maker.use.ui.fragment.donateFragment.OriginalityCrowdFundingFragment;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 爱心界面
 * Created by XT on 2016/9/28.
 */

public class DonateFragment extends BaseFragment {

    private View mMainView;
    private MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_donate, null);
        x.view().inject(this, mMainView);

        initView();
        return mMainView;
    }

    private void initView() {
        mActivity = (MainActivity) getActivity();

        //初始化toolbar
        Toolbar toolbar = (Toolbar) mMainView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);
        final ActionBar actionBar = mActivity.getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_icon_money);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //初始化viewpager
        ViewPager viewPager = (ViewPager) mMainView.findViewById(R.id.viewpager);
        if (viewPager != null) {
            Adapter adapter = new Adapter(mActivity.getSupportFragmentManager());
            adapter.addFragment(new CampusDynamicFragment(), "校园动态");
            adapter.addFragment(new DonationsDynamicFragment(), "捐赠动态");
            adapter.addFragment(new OriginalityCrowdFundingFragment(), "创意众筹");
            adapter.addFragment(new LoveCrowdFundingFragment(), "爱心众筹");
            viewPager.setAdapter(adapter);
        }
        //设置FloatingActionButton点击事件
        FloatingActionButton fab = (FloatingActionButton) mMainView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //将TabLayout与ViewPager绑定
        TabLayout tabLayout = (TabLayout) mMainView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sample_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Snackbar.make(mMainView, "bar bar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
        return true;
//        return super.onOptionsItemSelected(item);
    }*/

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
