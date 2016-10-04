package com.maker.use.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.maker.use.R;
import com.maker.use.utils.UIUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by XT on 2016/9/28.
 */

public class MessageFragment extends BaseFragment {

    @ViewInject(R.id.lv_message)
    private ListView lv_message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        x.view().inject(this,view);

        initView();
        return view;
    }

    private void initView() {
        lv_message.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(UIUtils.getContext(), R.layout.list_item_message, null);
            return view;
        }
    }
}
