package com.maker.use.ui.fragment.dynamicFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.ui.activity.OriginalityCrowdFundingDetailActivity;
import com.maker.use.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创意众筹页面
 */
public class OriginalityCrowdFundingFragment extends Fragment {

    private OriginalityAdapter mAdapter;
    private List<String> mDatas;
    private RecyclerView mMainView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = (RecyclerView) inflater.inflate(R.layout.viewpage_list_dynamic, container, false);

        getDataFromServer();
        initView();

        return mMainView;
    }

    /**
     * 从服务器上获取数据
     */
    protected void getDataFromServer() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initView() {
        mAdapter = new OriginalityAdapter(getActivity(), mDatas);
        mMainView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mMainView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OriginalityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(UIUtils.getContext(), OriginalityCrowdFundingDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * 动态众筹的适配器
     */
    static class OriginalityAdapter extends
            RecyclerView.Adapter<OriginalityAdapter.MyViewHolder> {

        private List<String> mDatas;
        private LayoutInflater mInflater;

        private List<Integer> mHeights;
        private OnItemClickListener mOnItemClickListener;

        public OriginalityAdapter(Context context, List<String> datas) {
            mInflater = LayoutInflater.from(context);
            mDatas = datas;

            mHeights = new ArrayList<Integer>();
            for (int i = 0; i < mDatas.size(); i++) {
                mHeights.add((int) (250 + Math.random() * 50));
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                    R.layout.list_item_dynamic_originalitycrowdfunding, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            //设置随机的高度实现瀑布流的效果
            ViewGroup.LayoutParams lp = holder.ll_root.getLayoutParams();
            lp.height = UIUtils.dip2px(mHeights.get(position));
            holder.ll_root.setLayoutParams(lp);

            // 如果设置了回调，则设置点击事件
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(holder.itemView, pos);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                        removeData(pos);
                        return false;
                    }
                });
            }


        }

        @Override
        public int getItemCount() {
            return 3;
        }

        public void removeData(int position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

        /**
         * 单击和长击接口
         */
        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_root;
            ImageView iv_img;
            TextView tv_target;
            TextView tv_have;
            TextView tv_remaining_time;
            TextView tv_support_number;
            TextView tv_title;

            public MyViewHolder(View view) {
                super(view);
                ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
                iv_img = (ImageView) view.findViewById(R.id.iv_img);
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_target = (TextView) view.findViewById(R.id.tv_target);
                tv_have = (TextView) view.findViewById(R.id.tv_have);
                tv_remaining_time = (TextView) view.findViewById(R.id.tv_remaining_time);
                tv_support_number = (TextView) view.findViewById(R.id.tv_support_number);

            }
        }
    }

}
