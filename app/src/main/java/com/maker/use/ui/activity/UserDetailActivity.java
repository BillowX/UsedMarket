package com.maker.use.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.maker.use.R;
import com.maker.use.global.ConstentValue;
import com.maker.use.manager.ActivityCollector;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.UIUtils;

import org.xutils.view.annotation.ContentView;

/**
 * 用户详情页
 * Created by XT on 2016/10/6.
 */
@ContentView(R.layout.activity_userdetail)
public class UserDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void logout(View view) {
        //移除用户信息
        SpUtil.remove("user");
        //更新登陆状态
        SpUtil.putBoolean(ConstentValue.IS_LOGIN, false);
        ActivityCollector.finishAll();
        UIUtils.getContext().startActivity(new Intent(UIUtils.getContext(), MainActivity.class));
        UIUtils.toast("注销成功");
    }
}
