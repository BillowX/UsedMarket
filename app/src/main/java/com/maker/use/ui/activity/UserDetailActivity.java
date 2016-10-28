package com.maker.use.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.supertextviewlibrary.SuperTextView;
import com.maker.use.R;
import com.maker.use.domain.User;
import com.maker.use.global.ConstentValue;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.manager.ActivityCollector;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.MD5;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 用户详情页
 * Created by XT on 2016/10/6.
 */
public class UserDetailActivity extends Activity implements View.OnClickListener {

    private User mUser;

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
        setContentView(R.layout.activity_userdetail);

        initData();
        initView();
    }

    private void initData() {
        mUser = (User) getIntent().getSerializableExtra("user");
    }

    private void initView() {
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/


        ImageView iv_user_head = (ImageView) findViewById(R.id.iv_user_head);
        TextView tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        SuperTextView stv_user_phone = (SuperTextView) findViewById(R.id.stv_user_phone);
        SuperTextView stv_user_age = (SuperTextView) findViewById(R.id.stv_user_age);
        SuperTextView stv_user_blood = (SuperTextView) findViewById(R.id.stv_user_blood);
        SuperTextView stv_user_constellation = (SuperTextView) findViewById(R.id.stv_user_constellation);
        SuperTextView stv_user_shippingAddress = (SuperTextView) findViewById(R.id.stv_user_shippingAddress);

        //头像
//        Glide.with(UIUtils.getContext()).load(UsedMarketURL.HEAD + mUser.getHeadPortrait().replace("_","")).centerCrop().into(iv_user_head);
        GlideUtils.setImg(this, UsedMarketURL.HEAD + mUser.getHeadPortrait().replace("_", ""), iv_user_head);
        //用户名
        tv_user_name.setText(mUser.getUsername());
        stv_user_phone.setRightString(mUser.getPhone());
    }

    //修改密码
    public void changePassword(View view) {
        showChangePasswordDialog();
    }

    private void showChangePasswordDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        final View view = View.inflate(this, R.layout.dialog_change_password, null);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_password = (EditText) view.findViewById(R.id.et_password);
                EditText et_new_password = (EditText) view.findViewById(R.id.et_new_password);
                String password = et_password.getText().toString();
                String newPassword = et_new_password.getText().toString();

                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(newPassword)) {
                    UIUtils.progressDialog(UserDetailActivity.this);
                    RequestParams params = new RequestParams(UsedMarketURL.CHANGE_PASSWORD);
                    params.addBodyParameter("userId", SpUtil.getUserId());
                    params.addBodyParameter("password", MD5.md5(password));
                    params.addBodyParameter("newPassword", MD5.md5(newPassword));

                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(final String result) {
                            if ("修改失败".equals(result)) {
                                UIUtils.toast("修改失败，请检查密码");
                            } else {
                                //保存用户信息
                                SpUtil.putString(ConstentValue.USER, result);
                                UIUtils.toast("修改成功");
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
                            dialog.dismiss();
                        }
                    });
                } else {
                    UIUtils.toast("不能为空!");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //注销按钮
    public void logout(View view) {
        //移除用户信息
        SpUtil.remove("user");
        //更新登陆状态
        SpUtil.putBoolean(ConstentValue.IS_LOGIN, false);
        ActivityCollector.finishAll();
        //跳转到主界面
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        UIUtils.toast("注销成功");
        finish();
    }

    @Override
    public void onClick(View v) {

    }
}
