package com.maker.use.ui.fragment.loveCrowdFundingFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demievil.library.RefreshLayout;
import com.maker.use.R;
import com.maker.use.domain.Comment;
import com.maker.use.ui.adapter.CommentListViewAdapter;
import com.maker.use.ui.fragment.BaseFragment;
import com.maker.use.utils.UIUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 爱心众筹 留言评论界面
 * Created by XT on 2016/10/22.
 */

public class CommentFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, View.OnClickListener {

    /*@ViewInject(R.id.ll_message_list)
   LinearLayout ll_message_list;*/
    @ViewInject(R.id.refresh_root)
    RefreshLayout refresh_root;
    @ViewInject(R.id.lv_comment)
    ListView lv_comment;

    private View mMainView;
    private ArrayList<Comment> mCommentDataList;
    private CommentListViewAdapter mCommentAdapter;
    private TextView tv_more;
    private ProgressBar pb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_originality_comment, null);
        x.view().inject(this, mMainView);
        initView();
        return mMainView;
    }

    private void initView() {
        //设置刷新布局
        refresh_root.setOnRefreshListener(this);
        refresh_root.setOnLoadListener(this);
        refresh_root.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_dark);
        View footerLayout = getActivity().getLayoutInflater().inflate(R.layout.list_item_more, null);
        tv_more = (TextView) footerLayout.findViewById(R.id.text_more);
        tv_more.setOnClickListener(this);
        pb = (ProgressBar) footerLayout.findViewById(R.id.load_progress_bar);
        lv_comment.addFooterView(footerLayout);
        refresh_root.setChildView(lv_comment);

        //在服务器上获取改物品的评论列表
        getCommentDataFromServer(0);

    }

    /**
     * 根据商品ID在服务器上获取改物品的评论列表
     *
     * @param limit 分页位置
     */
    private void getCommentDataFromServer(int limit) {
        if (limit == 0) {
            mCommentDataList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Comment comment = new Comment();
                comment.setPcontent("评论" + i);
                mCommentDataList.add(comment);
            }
            mCommentAdapter = new CommentListViewAdapter(UIUtils.getContext(), mCommentDataList);
            lv_comment.setAdapter(mCommentAdapter);
        } else if (limit == -1) {
            for (int i = 0; i < 3; i++) {
                Comment comment = new Comment();
                comment.setPcontent("刷新出来的评论" + i);
                mCommentDataList.add(0, comment);
                mCommentAdapter.notifyDataSetChanged();
            }
        } else if (limit == 1) {
            for (int i = 0; i < 3; i++) {
                Comment comment = new Comment();
                comment.setPcontent("加载更多出来的评论" + i);
                mCommentDataList.add(mCommentDataList.size(), comment);
                mCommentAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onRefresh() {
        getCommentDataFromServer(-1);
        refresh_root.setRefreshing(false);
    }

    @Override
    public void onLoad() {
//        getCommentDataFromServer(1);
        refresh_root.setLoading(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_more:
                getCommentDataFromServer(1);
                refresh_root.setLoading(false);
                break;
        }
    }
}
