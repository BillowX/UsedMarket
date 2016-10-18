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
public class UserDetailActivity extends Activity {

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

        ImageView iv_person_img = (ImageView) findViewById(R.id.iv_person_img);
        TextView tv_person_username = (TextView) findViewById(R.id.tv_person_username);
        TextView tv_person_sex = (TextView) findViewById(R.id.tv_person_sex);

        Glide.with(UIUtils.getContext()).load(UsedMarketURL.server_heart + "/head/" + mUser.username + "_head.jpg")
                .centerCrop().into(iv_person_img);
        tv_person_username.setText(mUser.username);
        tv_person_sex.setText(("man".equals(mUser.sex) ? "男" : "女"));
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
}
