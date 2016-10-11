package com.maker.use.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.maker.use.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by XISEVEN on 2016/10/11.
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
    @ViewInject(value = R.id.tv_title,parentId = R.id.setting_title)
    private TextView setting_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setting_title.setText("设置");

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_about:
                break;
            case R.id.setting_eceipt_address:
                break;
            case R.id.setting_update:
                break;
            default:
                break;
        }
    }
}
