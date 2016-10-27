package com.maker.use.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.maker.use.R;
import com.maker.use.domain.Commodity;
import com.maker.use.global.ConstentValue;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.FileUtil;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * 添加商品页面
 * Created by XT on 2016/10/16.
 */
@ContentView(R.layout.activity_addcommodity)
public class AddCommodityActivity extends BaseActivity {

    @ViewInject(R.id.et_name)
    EditText et_name;
    @ViewInject(R.id.et_price)
    EditText et_price;
    @ViewInject(R.id.et_num)
    EditText et_num;
    @ViewInject(R.id.spinner_category)
    Spinner spinner_category;
    @ViewInject(R.id.et_description)
    EditText et_description;
    @ViewInject(R.id.iv_img)
    ImageView iv_img;
    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.rv_img)
    RecyclerView rv_img;

    //存放所有选择的照片
    private ArrayList<String> allSelectedPicture = new ArrayList<String>();
    //存放从选择界面选择的照片
    private ArrayList<String> selectedPicture = new ArrayList<String>();

    private String mUserId;
    private Context context;

    private Commodity mCommodity;
    private PhotoAlbumAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //开启本activity的动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setExitTransition(new Explode());//new Slide()  new Fade()
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());
        }
        super.onCreate(savedInstanceState);
        context = this;

        //设置在activity启动的时候输入法默认是不开启的
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initData();
        initView();
    }

    private void initData() {
        mUserId = SpUtil.getUserId();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final GridLayoutManager manager = new GridLayoutManager(this, 3);
        rv_img.setLayoutManager(manager);
        mAdapter = new PhotoAlbumAdapter(this);
        rv_img.setAdapter(mAdapter);
        //设置条目边距
        rv_img.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                int spanSize = layoutParams.getSpanSize();
                int spanIndex = layoutParams.getSpanIndex();
                outRect.top = 10;
                if (spanSize != manager.getSpanCount()) {
                    //如果是左边的条目，则设置右边距
                    if (spanIndex == 0) {
                        outRect.left = 10;
                    } else if (spanIndex == 1) {
                        outRect.right = 10;
                        outRect.left = 10;
                    } else {
                        outRect.right = 10;
                    }
                }
            }
        });
    }

    /**
     * 检查数据
     */
    private boolean checkData() {
        String name = et_name.getText().toString();
        String price = et_price.getText().toString();
        String num = et_num.getText().toString();
        String description = et_description.getText().toString();
        String category = spinner_category.getSelectedItem().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(num) || TextUtils.isEmpty(description) || allSelectedPicture.size() < 1) {
            UIUtils.toast("还没填满呢");
            return false;
        } else if (allSelectedPicture.size() < 4) {
            UIUtils.toast("商品图片不能少于4张");
            return false;
        } else {
            mCommodity = new Commodity();
            mCommodity.commodityName = name;
            mCommodity.price = price;
            mCommodity.amount = num;
            mCommodity.category = category;
            mCommodity.description = description;
            return true;
        }
    }

    /**
     * 上传按钮触发
     * 上传到服务器
     *
     * @param view
     */
    public void uploadServer(View view) {
        if (!checkData() || mCommodity == null) {
            return;
        }
        UIUtils.progressDialog(this);
        RequestParams params = new RequestParams(UsedMarketURL.UPLOAD_COMMODITY);
        params.addBodyParameter("userId", mUserId);
        params.addBodyParameter("commodityName", mCommodity.commodityName);
        params.addBodyParameter("price", mCommodity.price);
        params.addBodyParameter("amount", mCommodity.amount);
        params.addBodyParameter("category", mCommodity.category);
        params.addBodyParameter("description", mCommodity.description);

        for (int i = 0; i < allSelectedPicture.size(); i++) {
            File imgFile = FileUtil.createImgFile("commodity_" + i);
            boolean writeFile = FileUtil.writeFile(imgFile, allSelectedPicture.get(i));
            if (!writeFile) {
                UIUtils.closeProgressDialog();
                UIUtils.toast("文件" + i + "克隆失败啦~");
                continue;
            }
            Log.e("addcommodity",i+"  success");
            params.addBodyParameter("images", imgFile);
        }
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                UIUtils.toast(result);
                if ("商品上传成功".equals(result)) {
                    Intent intent = new Intent(UIUtils.getContext(), CommodityListActivity.class);
                    intent.putExtra("userId", SpUtil.getUserId());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(AddCommodityActivity.this).toBundle());
                    } else {
                        startActivity(intent);
                    }
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.toast("网络出错啦");
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

    private void selectClick() {
        MultiImageSelector.create(UIUtils.getContext())
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .single() // 单选模式
                .multi() // 多选模式, 默认模式;
                .origin(allSelectedPicture) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(this, ConstentValue.REQUEST_IMAGE);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstentValue.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                selectedPicture = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                allSelectedPicture = selectedPicture;
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 展示用户选择上传的图片的Adapter
     */
    class PhotoAlbumAdapter extends RecyclerView.Adapter<PhotoAlbumAdapter.MyViewHolder> {

        private final LayoutInflater mLayoutInflater;

        public PhotoAlbumAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.grid_item_addcommodity, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (position == allSelectedPicture.size()) {
                holder.iv_img.setImageBitmap(BitmapFactory.decodeResource(UIUtils.getContext().getResources(), R.drawable.icon_addpic));
                holder.bt_delete.setVisibility(View.GONE);

                holder.iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectClick();
                    }
                });
                if (position == 9) {
                    holder.iv_img.setVisibility(View.GONE);
                }
            } else {
                GlideUtils.setImg(AddCommodityActivity.this,"file://" + allSelectedPicture.get(position),holder.iv_img);
//                ImageLoader.getInstance().displayImage("file://" + allSelectedPicture.get(position), holder.iv_img);
                holder.bt_delete.setVisibility(View.VISIBLE);
                holder.bt_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击后移除图片
                        allSelectedPicture.remove(position);
                        //更新UI
                        notifyDataSetChanged();
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return allSelectedPicture.size() + 1;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView iv_img;
            public Button bt_delete;

            public MyViewHolder(View itemView) {
                super(itemView);
                iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
                bt_delete = (Button) itemView.findViewById(R.id.bt_delete);
            }
        }
    }

}
