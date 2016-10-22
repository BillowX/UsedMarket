package com.maker.use.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maker.use.R;
import com.maker.use.domain.User;
import com.maker.use.global.ConstentValue;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.manager.ActivityCollector;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.UIUtils;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayout ll_user_head = (LinearLayout) findViewById(R.id.ll_user_head);
        LinearLayout ll_user_name = (LinearLayout) findViewById(R.id.ll_user_name);
        LinearLayout ll_user_realName = (LinearLayout) findViewById(R.id.ll_user_realName);
        LinearLayout ll_user_dormitory_num = (LinearLayout) findViewById(R.id.ll_user_dormitory_num);
        LinearLayout ll_user_phone = (LinearLayout) findViewById(R.id.ll_user_phone);
        LinearLayout ll_user_birthday = (LinearLayout) findViewById(R.id.ll_user_birthday);
        LinearLayout ll_user_registration_date = (LinearLayout) findViewById(R.id.ll_user_registration_date);
        ll_user_head.setOnClickListener(this);
        ll_user_name.setOnClickListener(this);
        ll_user_realName.setOnClickListener(this);
        ll_user_dormitory_num.setOnClickListener(this);
        ll_user_phone.setOnClickListener(this);
        ll_user_birthday.setOnClickListener(this);
        ll_user_registration_date.setOnClickListener(this);


        ImageView iv_user_head = (ImageView) findViewById(R.id.iv_user_head);
        TextView tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        TextView tv_user_sex = (TextView) findViewById(R.id.tv_user_sex);

        Glide.with(UIUtils.getContext()).load(UsedMarketURL.server_heart + "/head/" + mUser.username + "_head.jpg")
                .centerCrop().into(iv_user_head);
        tv_user_name.setText(mUser.username);
        tv_user_sex.setText(("man".equals(mUser.sex) ? "男" : "女"));
    }

    public void logout(View view) {
        //移除用户信息
        SpUtil.remove("user");
        //更新登陆状态
        SpUtil.putBoolean(ConstentValue.IS_LOGIN, false);
        ActivityCollector.finishAll();
        startActivity(new Intent(this, MainActivity.class));
        UIUtils.toast("注销成功");
        finish();
    }

    @Override
    public void onClick(View v) {

    }
}
