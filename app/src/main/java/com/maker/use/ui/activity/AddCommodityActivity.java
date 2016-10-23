package com.maker.use.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.maker.use.R;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.FileUtil;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

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

    //启动actviity的请求码
    private static final int REQUEST_IMAGE = 2;

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
    @ViewInject(R.id.gv_img)
    GridView gv_img;

    //存放所有选择的照片
    private ArrayList<String> allSelectedPicture = new ArrayList<String>();
    //存放从选择界面选择的照片
    private ArrayList<String> selectedPicture = new ArrayList<String>();

    private String mUsername;
    private GridAdapter gridAdapter;
    private Context context;

    private String mName;
    private String mPrice;
    private String mNum;
    private String mDescription;
    private String mCategory;

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
        mUsername = SpUtil.getUsername();
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

        gridAdapter = new GridAdapter();
        gv_img.setAdapter(gridAdapter);
    }

    private void selectClick() {
        MultiImageSelector.create(UIUtils.getContext())
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .single() // 单选模式
                .multi() // 多选模式, 默认模式;
                .origin(allSelectedPicture) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(this, REQUEST_IMAGE);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                selectedPicture = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for (String str : selectedPicture) {
                    if (!allSelectedPicture.contains(str)) {
                        allSelectedPicture.add(str);

                        gv_img.setAdapter(gridAdapter);
                    }
                }
            }
        }
    }

    /**
     * 检查数据
     */
    private boolean checkData() {
        mName = et_name.getText().toString();
        mPrice = et_price.getText().toString();
        mNum = et_num.getText().toString();
        mDescription = et_description.getText().toString();
        mCategory = spinner_category.getSelectedItem().toString();
        if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mPrice) || TextUtils.isEmpty(mNum) || TextUtils.isEmpty(mDescription) || allSelectedPicture.size() < 1) {
            UIUtils.toast("还没填满呢");
            return false;
        } else if (allSelectedPicture.size() < 4) {
            UIUtils.toast("商品图片不能少于4张");
            return false;
        } else {
            return true;
        }
    }

    public void upload(View view) {
        if (!checkData()) {
            return;
        }
        UIUtils.progressDialog(this);
        RequestParams params = new RequestParams(UsedMarketURL.server_heart + "/servlet/UploadCommodityServlet");
        params.addBodyParameter("username", mUsername);
        params.addBodyParameter("name", mName);
        params.addBodyParameter("price", mPrice);
        params.addBodyParameter("num", mNum);
        params.addBodyParameter("category", mCategory);
        params.addBodyParameter("description", mDescription);
        for (int i = 0; i < allSelectedPicture.size(); i++) {
            Log.e("原图片的路径：", allSelectedPicture.get(i));
            File imgFile = FileUtil.createImgFile("commodity_" + i);
            boolean writeFile = FileUtil.writeFile(imgFile, allSelectedPicture.get(i));
            Log.e("克隆图片的路径：", imgFile.getAbsolutePath());
            if (!writeFile) {
                UIUtils.closeProgressDialog();
                UIUtils.toast("文件" + i + "克隆失败啦~");
                continue;
            }
            params.addBodyParameter("img" + i, imgFile);
        }
//        params.addBodyParameter("imgurl", "commodity\\" + mUsername + "\\" + mImgFile.getName());
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                UIUtils.toast(result);
                if ("商品上传成功".equals(result)) {
                    Intent intent = new Intent(UIUtils.getContext(), CommodityListActivity.class);
                    intent.putExtra("username", SpUtil.getUsername());
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

    /**
     * 展示图片的GridView的适配器
     */
    class GridAdapter extends BaseAdapter {

        public LayoutInflater layoutInflater = LayoutInflater.from(context);

        @Override
        public int getCount() {
            return allSelectedPicture.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.grid_item_addcommodity, null);

                holder.image = (ImageView) convertView.findViewById(R.id.child_iv);
                holder.btn = (Button) convertView.findViewById(R.id.child_delete);

                holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == allSelectedPicture.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.icon_addpic));
                holder.btn.setVisibility(View.GONE);

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectClick();
                    }
                });
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                ImageLoader.getInstance().displayImage("file://" + allSelectedPicture.get(position),
                        holder.image);

                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击后移除图片
                        allSelectedPicture.remove(position);

                        //更新UI
                        gv_img.setAdapter(gridAdapter);
                    }
                });

            }
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView image;
        public Button btn;
    }

}
