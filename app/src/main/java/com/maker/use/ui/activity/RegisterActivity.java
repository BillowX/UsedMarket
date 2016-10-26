package com.maker.use.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.maker.use.R;
import com.maker.use.domain.User;
import com.maker.use.global.ConstentValue;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.FileUtil;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.MD5;
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
 * 注册页面
 * Created by XT on 2016/10/5.
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.bt_register)
    Button bt_register;
    @ViewInject(R.id.bt_editHead)
    Button bt_editHead;
    @ViewInject(R.id.iv_head)
    ImageView iv_head;
    //输入框
    @ViewInject(R.id.et_username)
    EditText et_username;
    @ViewInject(R.id.et_password)
    EditText et_password;
    @ViewInject(R.id.et_affirm_password)
    EditText et_affirm_password;
    @ViewInject(R.id.et_phone)
    EditText et_phone;
    @ViewInject(R.id.et_shippingAddress)
    EditText et_shippingAddress;
    //单选按钮
    @ViewInject(R.id.rb_man)
    RadioButton rb_man;
    @ViewInject(R.id.rb_woman)
    RadioButton rb_woman;

    private File mHeadFile;
    private ArrayList<String> selectedPicture;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //开启本activity的动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setExitTransition(new Explode());//new Slide()  new Fade()
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());
        }
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //用户选择头像
        bt_editHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelector.create(UIUtils.getContext())
                        .showCamera(true) // 是否显示相机. 默认为显示
                        .single() // 单选模式
                        .start(RegisterActivity.this, ConstentValue.REQUEST_IMAGE);
            }
        });
        //用户点击注册
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkFormData(v)) {
                    return;
                }
                if (user != null) {
                    UploadServer();
                }
            }
        });

    }

    /**
     * 检查用户填写的表单数据
     */
    private boolean checkFormData(View view) {
        //昵称、密码这两项时必填的，检查这两项
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String affirmPassword = et_affirm_password.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String shippingAddress = et_shippingAddress.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            UIUtils.snackBar(view, "请输入昵称");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            UIUtils.snackBar(view, "请输入密码");
            return false;
        }
        if (TextUtils.isEmpty(affirmPassword)) {
            UIUtils.snackBar(view, "请输入确认密码");
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            UIUtils.snackBar(view, "请输入您的电话号码");
            return false;
        }
        if (mHeadFile == null) {
            UIUtils.snackBar(view, "给自己选个头像吧");
            return false;
        }
        if (!password.equals(affirmPassword)) {
            UIUtils.snackBar(view, "两次密码不一致哦");
            return false;
        }

        int sex = 0;
        if (rb_man.isChecked()) {
            sex = 1;
        }

        user = new User();
        user.setSex(sex);
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        if (!TextUtils.isEmpty(shippingAddress)) {
            user.setShippingAddress(shippingAddress);
        }
        return true;
    }

    /**
     * 上传到服务器
     */
    private void UploadServer() {
        UIUtils.progressDialog(RegisterActivity.this);
        RequestParams params = new RequestParams(UsedMarketURL.REGISTER);
        params.addBodyParameter("headPortrait", mHeadFile);
        params.addBodyParameter("username", user.getUsername());
        params.addBodyParameter("password", MD5.md5(user.getPassword()));
        params.addBodyParameter("sex", user.getSex() + "");
        params.addBodyParameter("phone", user.getPhone());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                UIUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.toast(result);
                    }
                });
                if ("注册成功".equals(result)) {
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.toast("网络出错啦~");
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstentValue.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                selectedPicture = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                String str = selectedPicture.get(0);
                //显示在ImageView上
//                ImageLoader.getInstance().displayImage("file://" + str, iv_head);
                GlideUtils.setImg(RegisterActivity.this, "file://" + str, iv_head);
                //创建文件
                mHeadFile = FileUtil.createImgFile("head");
                FileUtil.writeFile(mHeadFile, str);
            }
        }
    }

}
