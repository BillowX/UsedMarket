package com.maker.use.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.ui.activity.LoginActivity;
import com.maker.use.ui.activity.MainActivity;
import com.maker.use.utils.UIUtils;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static android.R.attr.radius;


/**
 * Created by XT on 2016/9/26.
 */

public class MenuLeftFragment extends Fragment implements View.OnClickListener {
    @ViewInject(R.id.iv_icon)
    ImageView iv_icon;
    @ViewInject(R.id.tv_username)
    TextView tv_username;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_left, container, false);
        x.view().inject(this, view);
        iv_icon.setOnClickListener(this);

        initView();
        return view;
    }

    private void initView() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnLoginListener(new MainActivity.onLoginListener() {
            @Override
            public void onLogin(String username) {
                tv_username.setText("你好，" + username);
                ImageOptions imageOptions = new ImageOptions.Builder()
                        .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                        .setRadius(DensityUtil.dip2px(radius))
                        .setIgnoreGif(false)
                        .setCrop(true)//是否对图片进行裁剪
                        .setFailureDrawableId(R.drawable.register_default_head)
                        .setLoadingDrawableId(R.drawable.register_default_head)
                        .build();
                x.image().bind(iv_icon, "http://119.29.213.119:8080/UsedMarket/head/" + username + "_head.jpg", imageOptions);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_icon:
                startActivity(new Intent(UIUtils.getContext(), LoginActivity.class));
                break;
        }
    }

}
