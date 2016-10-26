package com.maker.use.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.domain.ShopCommodity;
import com.maker.use.ui.adapter.HomeAdapter;
import com.maker.use.ui.adapter.MenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺详情页面
 */
public class ShopDetailActivity extends BaseActivity {

    /**
     * 左侧菜单
     */
    private ListView lv_menu;
    /**
     * 右侧主菜
     */
    private ListView lv_home;
    private TextView tv_title;

    private MenuAdapter menuAdapter;
    private HomeAdapter homeAdapter;
    private int currentItem;
    /**
     * 数据源
     */
    private List<ShopCommodity> shopDatas;
    private String data[] = {"饮料", "零食", "凉拌", "宵夜", "冻品"};
    /**
     * 里面存放右边ListView需要显示标题的条目position
     */
    private ArrayList<Integer> showTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopdetail);
        setView();
        setData();
    }

    private void setView() {
        // TODO Auto-generated method stub
        lv_menu = (ListView) findViewById(R.id.lv_menu);
        tv_title = (TextView) findViewById(R.id.tv_titile);
        lv_home = (ListView) findViewById(R.id.lv_home);
        tv_title.setText(data[0]);

        shopDatas = new ArrayList<ShopCommodity>();
        for (int i = 0; i < data.length; i++) {
            shopDatas.add(new ShopCommodity("类型1", "黄焖鸡微辣" + i, "微辣感觉刚刚好，颜值很高，味道也很好，为你献上美味的食品" + i, "¥  15" + i, null));
        }
        for (int i = 0; i < 3; i++) {
            shopDatas.add(new ShopCommodity("类型2", "黄焖鸡微辣" + i, "微辣感觉刚刚好，颜值很高，味道也很好，为你献上美味的食品" + i, "¥  15" + i, null));
        }
        for (int i = 0; i < data.length - 1; i++) {
            shopDatas.add(new ShopCommodity("类型3", "黄焖鸡微辣" + i, "微辣感觉刚刚好，颜值很高，味道也很好，为你献上美味的食品" + i, "¥  15" + i, null));
        }
        for (int i = 0; i < data.length - 1; i++) {
            shopDatas.add(new ShopCommodity("类型4", "黄焖鸡微辣" + i, "微辣感觉刚刚好，颜值很高，味道也很好，为你献上美味的食品" + i, "¥  15" + i, null));
        }
        for (int i = 0; i < data.length - 1; i++) {
            shopDatas.add(new ShopCommodity("类型5", "黄焖鸡微辣" + i, "微辣感觉刚刚好，颜值很高，味道也很好，为你献上美味的食品" + i, "¥  15" + i, null));
        }
        showTitle = new ArrayList<Integer>();
        for (int i = 0; i < shopDatas.size(); i++) {
            if (i == 0) {
                showTitle.add(i);
                System.out.println(i + "aa");
            } else if (!TextUtils.equals(shopDatas.get(i).getType(), shopDatas.get(i - 1).getType())) {
                showTitle.add(i);
                System.out.println(i + "bb");
            }
        }
    }

    private void setData() {
        tv_title.setText(shopDatas.get(0).getType());
        menuAdapter = new MenuAdapter(this);
        homeAdapter = new HomeAdapter(this, shopDatas);
        lv_menu.setAdapter(menuAdapter);
        lv_home.setAdapter(homeAdapter);
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                menuAdapter.setSelectItem(arg2);
                menuAdapter.notifyDataSetInvalidated();
                lv_home.setSelection(showTitle.get(arg2));

                tv_title.setText(data[arg2]);

            }
        });
        lv_home.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    return;
                }
                int current = showTitle.indexOf(firstVisibleItem);
                System.out.println(current + "dd" + firstVisibleItem);
                if (currentItem != current && current >= 0) {
                    currentItem = current;
                    tv_title.setText(data[current]);
                    menuAdapter.setSelectItem(currentItem);
                    menuAdapter.notifyDataSetInvalidated();
                }
            }
        });
    }
}
