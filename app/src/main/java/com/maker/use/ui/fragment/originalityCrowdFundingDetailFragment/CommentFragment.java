package com.maker.use.ui.fragment.originalityCrowdFundingDetailFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.maker.use.R;
import com.maker.use.ui.adapter.CommentAdapter;
import com.maker.use.ui.fragment.BaseFragment;
import com.maker.use.ui.view.DividerLine;
import com.maker.use.utils.UIUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 留言评论界面
 * Created by XT on 2016/10/22.
 */

public class CommentFragment extends BaseFragment {

    @ViewInject(R.id.ll_message_list)
    LinearLayout ll_message_list;

    private View mMainView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_originality_comment, null);
        x.view().inject(this, mMainView);
        initView();
        return mMainView;
    }

    private void initView() {
        //留言区
        RecyclerView recyclerView = new RecyclerView(UIUtils.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //如果没有留言，则显示EmptyAdapter
//            recyclerView.setAdapter(new EmptyAdapter());
        recyclerView.setAdapter(new CommentAdapter(UIUtils.getContext()));
        //设置条目之间的分割线
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(0xFFDDDDDD);
        recyclerView.addItemDecoration(dividerLine);
        ll_message_list.addView(recyclerView);
    }
}
