package com.maker.use.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.domain.Comment;
import com.maker.use.global.ConstentValue;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.ui.activity.UserDetailActivity;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.TimeUtil;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.maker.use.R.drawable.delete;

/**
 * 评论列表适配器
 * Created by jzh on 2015/9/28.
 */
public class CommentListViewAdapter extends BaseAdapter {
    Context context;
    List<Comment> mComments;
    String mUserId;

    public CommentListViewAdapter(Context context, List<Comment> commentArrayList, String userId) {
        this.context = context;
        this.mComments = commentArrayList;
        this.mUserId = userId;
    }


    public void setList(ArrayList<Comment> list) {
        mComments = list;
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_comment, null);
            holder.civ_user_head = (CircleImageView) convertView.findViewById(R.id.civ_user_head);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_floor = (TextView) convertView.findViewById(R.id.tv_floor);
            holder.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GlideUtils.setCircleImageViewImg(context, UsedMarketURL.HEAD + mComments.get(position).getHeadPortrait(), holder.civ_user_head);
        holder.civ_user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra("userId", mComments.get(position).getUserId());
                context.startActivity(intent);
            }
        });
        holder.tv_floor.setText("第" + (mComments.size() - position) + "楼");
        holder.tv_comment_content.setText(mComments.get(position).getCommentText());
        holder.tv_username.setText(mComments.get(position).getUsername());
        if (mComments.get(position).getCommentLocation() != null) {
            holder.tv_location.setText(mComments.get(position).getCommentLocation());
        }
        holder.tv_time.setText(TimeUtil.format(mComments.get(position).getCommentDate()));


        //根据评论的用户ID与商品发布者的ID比较
        if (mComments.get(position).getUserId().equals(mUserId)) {
            Drawable floor = context.getResources().getDrawable(R.drawable.icon_floor);
            floor.setBounds(0, 0, floor.getMinimumWidth(), floor.getMinimumHeight());
            holder.tv_username.setCompoundDrawables(null, null, floor, null);
        } else {
            holder.tv_username.setCompoundDrawables(null, null, null, null);
        }
        //如果是自己发布的评论，则可以删除
        if (SpUtil.getBoolean(ConstentValue.IS_LOGIN, false) && mComments.get(position).getUserId().equals(SpUtil.getUserId())) {
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   // showDeleteWindow(mComments.get(position).getCommentId());
                    return false;
                }
            });
        }

        return convertView;
    }

    //显示删除的窗口
    private void showDeleteWindow(final String commentId) {
        Dialog alertDialog = new AlertDialog.Builder(context).
                setTitle("确定删除？").
                setMessage("您确定删除该条留言吗？").
                setIcon(delete).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteComment(dialog, commentId);
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).
                create();
        alertDialog.show();
    }

    private void deleteComment(final DialogInterface dialog, String commentId) {
        UIUtils.progressDialog(context);
        RequestParams requestParams = new RequestParams(UsedMarketURL.DELETE_COMMENT);
        requestParams.addBodyParameter("commentId", commentId);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UIUtils.toast("删除成功");
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.toast("网络出错了");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                UIUtils.closeProgressDialog();
            }
        });
    }


    private class ViewHolder {
        private TextView tv_username;
        private TextView tv_location;
        private TextView tv_time;
        private TextView tv_floor;
        private TextView tv_comment_content;
        private CircleImageView civ_user_head;
    }

}
