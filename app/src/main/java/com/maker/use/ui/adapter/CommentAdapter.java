package com.maker.use.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.domain.Comment;

import java.util.ArrayList;

/**
 * 评论列表适配器
 * Created by XT on 2016/10/23.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private final TypedValue mTypedValue = new TypedValue();
    private final ArrayList<Comment> mCommentList;
    private int mBackground;

    public CommentAdapter(Context context, ArrayList<Comment> commentList) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;

        mCommentList = commentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment, parent, false);
        view.setBackgroundResource(mBackground);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_comment_center.setText(mCommentList.get(position).getPcontent());
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_comment_center;
        View rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            tv_comment_center = (TextView) itemView.findViewById(R.id.tv_comment_center);
        }
    }
}
