package com.maker.use.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.ui.activity.CommodityListActivity;
import com.maker.use.utils.UIUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * 分类界面
 * Created by XT on 2016/9/28.
 */

public class ClassifyFragment extends BaseFragment {
    @ViewInject(R.id.gv_classify)
    GridView gv_classify;

    private String[] mItemArray;
    private int[] mItemImgArray = {
            R.drawable.classify_1, R.drawable.classify_2,
            R.drawable.classify_3, R.drawable.classify_4,
            R.drawable.classify_5, R.drawable.classify_6,
            R.drawable.classify_7, R.drawable.classify_8,
            R.drawable.classify_9, R.drawable.classify_10,
            R.drawable.classify_11, R.drawable.classify_12,
            R.drawable.classify_13, R.drawable.classify_14,
            R.drawable.classify_15
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        x.view().inject(this, view);
        initView();
        return view;
    }

    private void initView() {
        mItemArray = UIUtils.getStringArray(R.array.classify);
        gv_classify.setAdapter(new MyAdapter());
        gv_classify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UIUtils.getContext(), CommodityListActivity.class);
                intent.putExtra("category",mItemArray[position]);
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mItemArray.length;
        }

        @Override
        public String getItem(int position) {
            return mItemArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(UIUtils.getContext(), R.layout.grid_item_classify, null);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            iv_icon.setImageResource(mItemImgArray[position]);
            tv_title.setText(getItem(position));

            return view;
        }
    }
}
