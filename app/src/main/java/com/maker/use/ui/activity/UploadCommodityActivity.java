package com.maker.use.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.maker.use.R;
import com.maker.use.global.UsedMarketURL;
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

/**
 * 上传商品页面
 * Created by XT on 2016/10/7.
 */
@ContentView(R.layout.activity_uploadcommodity)
public class UploadCommodityActivity extends BaseActivity {

    private static final int REQUEST_IMAGE = 1;
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
    
    private ArrayList<String> mPath;
    private String mName;
    private String mPrice;
    private String mNum;
    private String mDescription;
    private String mCategory;
    private String mUsername;
    private File mImgFile;
    private Uri fileUri = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initView() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initData() {
        mUsername = SpUtil.getUsername();
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
        params.addBodyParameter("img", mImgFile);
        params.addBodyParameter("imgurl", "commodity\\" + mUsername + "\\" + mImgFile.getName());
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                UIUtils.toast(result);
                if ("商品上传成功".equals(result)) {
                    Intent intent = new Intent(UIUtils.getContext(), CommodityListActivity.class);
                    intent.putExtra("username", SpUtil.getUsername());
                    startActivity(intent);
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
     * 检查数据
     */
    private boolean checkData() {
        mName = et_name.getText().toString();
        mPrice = et_price.getText().toString();
        mNum = et_num.getText().toString();
        mDescription = et_description.getText().toString();
        mCategory = spinner_category.getSelectedItem().toString();
        if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mPrice) || TextUtils.isEmpty(mNum) || TextUtils.isEmpty(mDescription) || mImgFile == null) {
            UIUtils.toast("还没填满呢");
            return false;
        } else {
            return true;
        }
    }

    public void choiceImg(View view) {
        mName = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(mName)) {
            UIUtils.toast("先输入商品名，才能选择图片！");
            return;
        }
        gotoChoiceImg();
    }

    private void gotoChoiceImg() {
        MultiImageSelector.create(UIUtils.getContext())
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .single() // 单选模式
                .multi() // 多选模式, 默认模式;
                .origin(mPath) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(this, REQUEST_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
    }

}
