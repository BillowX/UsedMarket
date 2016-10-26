package com.maker.use.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.domain.ShopCommodity;

import java.util.List;

/**
 * 右侧主界面ListView的适配器
 *
 * @author Administrator
 */
public class HomeAdapter extends BaseAdapter {
    private Context context;
    private List<ShopCommodity> shopDatas;

    public HomeAdapter(Context context, List<ShopCommodity> shopDatas) {
        this.context = context;
        this.shopDatas = shopDatas;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (shopDatas != null) {
            return shopDatas.size();
        } else {
            return 10;
        }
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHold holder = null;
        if (arg1 == null) {
            arg1 = View.inflate(context, R.layout.list_item_shop_home, null);
            holder = new ViewHold();
            holder.tv_title = (TextView) arg1.findViewById(R.id.item_home_title);
            holder.tv_name = (TextView) arg1.findViewById(R.id.item_home_name);
            holder.tv_desc = (TextView) arg1.findViewById(R.id.item_home_description);
            arg1.setTag(holder);
        } else {
            holder = (ViewHold) arg1.getTag();
        }
        holder.tv_name.setText(shopDatas.get(arg0).getName());
        holder.tv_title.setText(shopDatas.get(arg0).getType());
        holder.tv_desc.setText(shopDatas.get(arg0).getDesc());
        if (arg0 == 0) {
            holder.tv_title.setVisibility(View.VISIBLE);
        } else if (!TextUtils.equals(shopDatas.get(arg0).getType(), shopDatas.get(arg0 - 1).getType())) {
            holder.tv_title.setVisibility(View.VISIBLE);
        } else {
            holder.tv_title.setVisibility(View.GONE);
        }
        return arg1;
    }

    private static class ViewHold {
        private TextView tv_title;
        private TextView tv_name;
        private TextView tv_desc;
    }
}
