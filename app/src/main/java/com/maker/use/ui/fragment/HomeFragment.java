package com.maker.use.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lzy.widget.HeaderScrollHelper;
import com.lzy.widget.tab.CircleIndicator;
import com.maker.use.R;
import com.maker.use.domain.Top;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.view.MyXRecyclerView;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 主页界面
 * Created by XT on 2016/9/28.
 */

public class HomeFragment extends BaseFragment implements HeaderScrollHelper.ScrollableContainer {

    @ViewInject(R.id.rl_root)
    RelativeLayout rl_root;

    @ViewInject(R.id.pagerHeader)
    private ViewPager pagerHeader;
    @ViewInject(R.id.ci)
    private CircleIndicator ci;

    private ArrayList<Top.img> mImgs;
    private MyXRecyclerView mMyXRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_home, null);
        x.view().inject(this, mainView);

        initView();

        return mainView;
    }

    public void initView() {
        //添加MyXRecyclerView
        HashMap<String, String> map = new HashMap<>();
        map.put("all","all");
        mMyXRecyclerView = new MyXRecyclerView(UIUtils.getContext(), map);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMyXRecyclerView.setLayoutParams(layoutParams);
        rl_root.addView(mMyXRecyclerView, layoutParams);
        //添加头布局
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_header_home, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
        x.view().inject(this, header);
        getDataFromServer();
        mMyXRecyclerView.addHeaderView(header);

        //空界面
    }

    /**
     * 从服务器上获取数据
     */
    private void getDataFromServer() {
        //获取TOP10图片的地址
        String url = UsedMarketURL.VPImgUrl;
        x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.toast("网络出错啦~");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析从服务器获取的JSON数据
     *
     * @param result
     */
    private void processData(String result) {
        Gson gson = new Gson();
        Top top = gson.fromJson(result, Top.class);
        mImgs = top.imgs;
        pagerHeader.setAdapter(new HeaderAdapter());
        ci.setViewPager(pagerHeader);
    }

    @Override
    public View getScrollableView() {
        return mMyXRecyclerView;
    }

    private class HeaderAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            x.image().bind(imageView, UsedMarketURL.url_heart + mImgs.get(position).imgUrl);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mImgs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}