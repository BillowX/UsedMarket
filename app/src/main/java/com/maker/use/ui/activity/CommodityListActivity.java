package com.maker.use.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maker.use.R;
import com.maker.use.domain.Commodity;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 我的发布页面
 * Created by XT on 2016/10/8.
 */
@ContentView(R.layout.activity_myissue)
public class CommodityListActivity extends BaseActivity {
    @ViewInject(R.id.lv_myissue)
    ListView lv_myissue;

    private List<Commodity> mCommoditys;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        RequestParams params = new RequestParams(UsedMarketURL.server_heart + "/servlet/FindCommodityServlet");

        String category = getIntent().getStringExtra("category");
        String username = getIntent().getStringExtra("username");
        if (!TextUtils.isEmpty(username)) {
            params.addQueryStringParameter("username", username);
        } else if (!TextUtils.isEmpty(category)) {
            params.addQueryStringParameter("category", category);
        }

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if ("无数据".equals(result)) {
                    UIUtils.toast(result);
                } else {
                    Gson gson = new Gson();
                    mCommoditys = gson.fromJson(result, new TypeToken<List<Commodity>>() {
                    }.getType());
                    lv_myissue.setAdapter(new myLvAdapter());
                }

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

            }
        });
    }

    class myLvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCommoditys.size();
        }

        @Override
        public Commodity getItem(int position) {
            return mCommoditys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(UIUtils.getContext(), R.layout.list_item_commoditylist, null);
            ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_description = (TextView) view.findViewById(R.id.tv_description);
            TextView tv_price = (TextView) view.findViewById(R.id.tv_price);

            Log.e("test", UsedMarketURL.server_heart + "/" + getItem(position).imgurl);
            //用户头像
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setIgnoreGif(false)
                    .setFailureDrawableId(R.drawable.error)
                    .setLoadingDrawableId(R.drawable.loading)
                    .build();
            x.image().bind(iv_img, UsedMarketURL.server_heart + "//" + getItem(position).imgurl, imageOptions);
            tv_name.setText(getItem(position).name);
            tv_description.setText(getItem(position).description);
            tv_price.setText(String.valueOf(getItem(position).price));
            return view;
        }
    }
}
