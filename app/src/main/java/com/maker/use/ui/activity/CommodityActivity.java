package com.maker.use.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.maker.use.R;
import com.maker.use.domain.Commodity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by XISEVEN on 2016/10/9.
 */
@ContentView(R.layout.activity_commodity)
public class CommodityActivity extends BaseActivity {
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
    @ViewInject(R.id.recView_goods_img)
    private XRecyclerView recView_goods_img;
    @ViewInject(R.id.tv_goods_description)
    private TextView tv_goods_description;

    @ViewInject(value = R.id.tv_title, parentId = R.id.goods_title)
    private TextView tv_goods_title;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_goods_title.setText("商品详情");
        Bundle mBundle = getIntent().getBundleExtra("commodityBundle");
        Commodity commodity = (Commodity) mBundle.get("commodity");
        if (commodity != null) {
            x.image().bind(iv_userHeadimg, commodity.userHeadImg);
            tv_userName.setText(commodity.userName);
            tv_goods_price.setText("" + commodity.price);
            tv_goods_time.setText(commodity.time);
            tv_goods_name.setText(commodity.name);
            tv_goods_description.setText(commodity.description);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recView_goods_img.setLayoutManager(layoutManager);
            recView_goods_img.setAdapter(new RecyclerView.Adapter() {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                }

                @Override
                public int getItemCount() {
                    return 0;
                }
            });
        }
    }
}
