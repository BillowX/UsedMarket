package com.maker.use.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.maker.use.R;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.FileUtil;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import static com.maker.use.global.ConstentValue.CHOICE_HEAD_DIALOG;

/**
 * 上传商品页面
 * Created by XT on 2016/10/7.
 */
@ContentView(R.layout.activity_uploadcommodity)
public class UploadCommodityActivity extends BaseActivity {

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

    private String mName;
    private String mPrice;
    private String mNum;
    private String mDescription;
    private String mCategory;
    private String mUsername;

    private File mImgFile;
    private Uri fileUri = null;
    private String mImgPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        mUsername = SpUtil.getUsername();
    }

    public void upload(View view) {
        if (!checkData()) {
            return;
        }

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
        showDialog(CHOICE_HEAD_DIALOG);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case CHOICE_HEAD_DIALOG: {
                builder.setTitle("选择头像来源")
                        .setItems(new String[]{"相册", "拍照"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    //跳转到图片浏览器的应用，选取要发送的图片
                                    Intent i = new Intent();
                                    i.setType("image/*");
                                    i.putExtra("return-data", true);
                                    i.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(i, Activity.DEFAULT_KEYS_SHORTCUT);
                                } else if (which == 1) { //拍照
                                    Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
                                    mImgFile = FileUtil.createHeadFile("_commodity");
                                    fileUri = Uri.fromFile(mImgFile);
                                    it.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                    startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);
                                }
                            }
                        });
            }
            break;
        }
        return builder.create();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        //通过“图片浏览器”获得自己的头像图片
        if (requestCode == Activity.DEFAULT_KEYS_SHORTCUT) {
            Uri uri = data.getData();
            mImgFile = FileUtil.createHeadFile("_commodity");
            mImgPath = mImgFile.getAbsolutePath();
            boolean result = FileUtil.writeFile(getContentResolver(), mImgFile, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(mImgFile.getAbsolutePath());
            if (bitmap != null) {
                iv_img.setImageBitmap(bitmap);
            }
        }
        //通过手机的拍照功能，获得自己的头像图片
        else if (requestCode == Activity.DEFAULT_KEYS_DIALER) {
            mImgPath = fileUri.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(mImgPath);
            if (bitmap != null) {
                iv_img.setImageBitmap(bitmap);
            }
        }
    }

}
