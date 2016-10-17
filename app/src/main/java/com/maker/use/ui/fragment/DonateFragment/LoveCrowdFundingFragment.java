package com.maker.use.ui.fragment.donateFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 创意众筹页面
 */
public class LoveCrowdFundingFragment extends Fragment {

    private StaggeredHomeAdapter mStaggeredHomeAdapter;
    private List<String> mDatas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();

        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_cheese_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    protected void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        mStaggeredHomeAdapter = new StaggeredHomeAdapter(getActivity(), mDatas);


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(mStaggeredHomeAdapter);
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    static class StaggeredHomeAdapter extends
            RecyclerView.Adapter<StaggeredHomeAdapter.MyViewHolder> {

        private List<String> mDatas;
        private LayoutInflater mInflater;

        private List<Integer> mHeights;
        private OnItemClickLitener mOnItemClickLitener;

        public StaggeredHomeAdapter(Context context, List<String> datas) {
            mInflater = LayoutInflater.from(context);
            mDatas = datas;

            mHeights = new ArrayList<Integer>();
            for (int i = 0; i < mDatas.size(); i++) {
                mHeights.add((int) (350 + Math.random() * 120));
            }
        }

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                    R.layout.list_item_donate_lovecrowdfunding, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            ViewGroup.LayoutParams lp = holder.ll_root.getLayoutParams();
            lp.height = UIUtils.dip2px(mHeights.get(position));

            holder.ll_root.setLayoutParams(lp);
//            holder.tv_title.setText(mDatas.get(position));

            // 如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                        removeData(pos);
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public void addData(int position) {
            mDatas.add(position, "Insert One");
            mHeights.add((int) (100 + Math.random() * 300));
            notifyItemInserted(position);
        }

        public void removeData(int position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

        public interface OnItemClickLitener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_title;
            LinearLayout ll_root;

            public MyViewHolder(View view) {
                super(view);
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                ll_root = (LinearLayout) view.findViewById(R.id.ll_root);

            }
        }
    }

}
