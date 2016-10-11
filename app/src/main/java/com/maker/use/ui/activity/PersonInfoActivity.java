package com.maker.use.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.domain.User;
import com.maker.use.global.UsedMarketURL;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static android.R.attr.radius;

/**
 * Created by XISEVEN on 2016/10/11.
 */
@ContentView(R.layout.activity_personinfo)
public class PersonInfoActivity extends BaseActivity {
    @ViewInject(value = R.id.tv_title, parentId = R.id.personInfo_title)
    private TextView personInfo_title;
    @ViewInject(R.id.iv_person_img)
    private ImageView iv_person_img;
    @ViewInject(R.id.tv_person_username)
    private TextView tv_person_username;
    @ViewInject(R.id.tv_person_sex)
    private TextView tv_person_sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        User user = (User) getIntent().getSerializableExtra("user");
        personInfo_title.setText("用户信息");
        if (user != null) {
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setRadius(DensityUtil.dip2px(radius))
                    .setIgnoreGif(false)
                    .setCrop(true)//是否对图片进行裁剪
                    .setFailureDrawableId(R.drawable.register_default_head)
                    .setLoadingDrawableId(R.drawable.register_default_head)
                    .build();
            x.image().bind(iv_person_img, UsedMarketURL.server_heart + "/head/" + user.username + "_head.jpg", imageOptions);
            tv_person_username.setText(user.username);
            tv_person_sex.setText(user.sex);
        }

    }

    /**
     * 点击监听响应修改的操作
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userImg:
                break;
            case R.id.userName:
                break;
            case R.id.userSex:
                break;
            case R.id.passWord:
                break;
            case R.id.cancel_account:
            default:
                break;
        }
    }

}
