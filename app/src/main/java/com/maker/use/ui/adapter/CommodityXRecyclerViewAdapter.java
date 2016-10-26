package com.maker.use.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.domain.Commodity;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.TimeUtil;
import com.maker.use.utils.UIUtils;

import java.util.List;


/**
 * 商品列表适配器
 * Created by XT on 2016/10/22.
 */

public class CommodityXRecyclerViewAdapter extends RecyclerView.Adapter<CommodityXRecyclerViewAdapter.MyViewHolder> {

    List<Commodity> CommodityList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerViewItemLongClickListener mOnItemLongClickListener;

    public CommodityXRecyclerViewAdapter(List<Commodity> list) {
        this.CommodityList = list;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_commoditylist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //发布者头像
        /*Glide.with(UIUtils.getContext()).load(UsedMarketURL.HEAD + CommodityList.get(position).headPortrait)
                .centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.iv_user_head);*/
        GlideUtils.setCircleImageViewImg(UIUtils.getContext(), UsedMarketURL.HEAD + CommodityList.get(position).headPortrait, holder.iv_user_head);
        //发布者用户名
        holder.tv_username.setText(CommodityList.get(position).username);
        holder.tv_time.setText(TimeUtil.format(CommodityList.get(position).launchDate));
        //商品
        holder.tv_name.setText(CommodityList.get(position).commodityName);
        holder.tv_description.setText(CommodityList.get(position).description);
        holder.tv_price.setText("￥ " + CommodityList.get(position).price);
        //商品图片
        String[] splitImgUrl = CommodityList.get(position).images.split(";");
        /*ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setIgnoreGif(false)
                .setFailureDrawableId(R.drawable.error)
                .setLoadingDrawableId(R.drawable.loading)
                .setSize(0, 0)//设置图片大小
                .build();*/
        for (int i = 0; i < 9; i++) {
            if (i < splitImgUrl.length) {
//                x.image().bind(holder.ivPics[i], UsedMarketURL.server_heart + "//" + splitImgUrl[i], imageOptions);
                /*Glide.with(UIUtils.getContext()).load(UsedMarketURL.server_heart + "//" + splitImgUrl[i])
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(holder.ivPics[i]);*/
                GlideUtils.setImg(UIUtils.getContext(), UsedMarketURL.server_heart + "//" + splitImgUrl[i], holder.ivPics[i]);
            } else {
                holder.ivPics[i].setVisibility(View.GONE);
            }
        }

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(R.string.delete_commodity, CommodityList.get(position));
        holder.itemView.setTag(R.string.delete_position, position);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return CommodityList.size();
    }

    public void add(List<Commodity> list) {
        if (CommodityList != null) {
            CommodityList.addAll(list);
            notifyDataSetChanged();
//            notifyItemInserted(CommodityList.size());
        }
    }

    public void add(int position, Commodity commodity) {
        if (CommodityList != null) {
            CommodityList.add(position, commodity);
            notifyItemInserted(position);
        }
    }

    public void delete(int position) {
        if (CommodityList != null) {
            CommodityList.remove(position);
            Log.e("index", position + "");
            notifyItemRangeRemoved(position, CommodityList.size());
//            notifyItemRangeChanged(1,CommodityList.size());
//            notifyItemRemoved(position);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Commodity commodity);
    }

    public interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(View view, Commodity commodity, int position);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        LinearLayout ll_img;
        ImageView[] ivPics;
        TextView tv_username;
        TextView tv_name;
        TextView tv_description;
        TextView tv_price;
        TextView tv_time;
        ImageView iv_user_head;

        public MyViewHolder(final View itemView) {
            super(itemView);

            iv_user_head = (ImageView) itemView.findViewById(R.id.iv_user_head);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);

            ivPics = new ImageView[9];
            ivPics[0] = (ImageView) itemView.findViewById(R.id.iv_pic0);
            ivPics[1] = (ImageView) itemView.findViewById(R.id.iv_pic1);
            ivPics[2] = (ImageView) itemView.findViewById(R.id.iv_pic2);
            ivPics[3] = (ImageView) itemView.findViewById(R.id.iv_pic3);
            ivPics[4] = (ImageView) itemView.findViewById(R.id.iv_pic4);
            ivPics[5] = (ImageView) itemView.findViewById(R.id.iv_pic5);
            ivPics[6] = (ImageView) itemView.findViewById(R.id.iv_pic6);
            ivPics[7] = (ImageView) itemView.findViewById(R.id.iv_pic7);
            ivPics[8] = (ImageView) itemView.findViewById(R.id.iv_pic8);

            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);

            ll_img = (LinearLayout) itemView.findViewById(R.id.ll_img);

            ll_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取数据
                        mOnItemClickListener.onItemClick(iv_user_head, (Commodity) itemView.getTag(R.string.delete_commodity));
                    }
                }
            });
            ll_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        //注意这里使用getTag方法获取数据
                        mOnItemLongClickListener.onItemLongClick(itemView, (Commodity) itemView.getTag(R.string.delete_commodity), (int) itemView.getTag(R.string.delete_position));
                    }
                    return true;
                }
            });
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(iv_user_head, (Commodity) v.getTag(R.string.delete_commodity));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemLongClickListener.onItemLongClick(v, (Commodity) v.getTag(R.string.delete_commodity), (int) v.getTag(R.string.delete_position));
            }
            return true;
        }

    }

}