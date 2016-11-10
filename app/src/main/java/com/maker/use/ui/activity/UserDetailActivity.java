package com.maker.use.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.supertextviewlibrary.SuperTextView;
import com.maker.use.R;
import com.maker.use.domain.User;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.GsonUtils;
import com.maker.use.utils.TimeUtil;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.Calendar;

import static com.maker.use.R.id.stv_user_age;

/**
 * 普通用户详情页面
 * Created by XT on 2016/11/5.
 */
@ContentView(R.layout.activity_userdetail)
public class UserDetailActivity extends BaseActivity {

    private ImageView mIv_user_head;
    private ImageView iv_user_sex;
    private TextView mTv_user_name;
    private TextView mTv_personalized_signature;
    private SuperTextView mStv_user_age;
    private SuperTextView mStv_user_constellation;
    private SuperTextView stv_registration_date;

    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = new User();
        initData();
        initView();
    }

    private void initData() {
        String userId = getIntent().getStringExtra("userId");

        //从服务器获取对应用户的信息
        UIUtils.progressDialog(this);
        RequestParams requestParams = new RequestParams(UsedMarketURL.FIND_USER_INFO);
        requestParams.addBodyParameter("userId", userId);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mUser = GsonUtils.getGson().fromJson(result, User.class);
                initViewData();
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

    private void initView() {
        mIv_user_head = (ImageView) findViewById(R.id.iv_user_head);
        iv_user_sex = (ImageView) findViewById(R.id.iv_user_sex);
        mTv_user_name = (TextView) findViewById(R.id.tv_user_name);
        mTv_personalized_signature = (TextView) findViewById(R.id.tv_personalized_signature);
        mStv_user_age = (SuperTextView) findViewById(stv_user_age);
        mStv_user_constellation = (SuperTextView) findViewById(R.id.stv_user_constellation);
        stv_registration_date = (SuperTextView) findViewById(R.id.stv_registration_date);
    }

    private void initViewData() {
        //头像
        GlideUtils.setImg(this, UsedMarketURL.HEAD + mUser.getNarrowHeadPortraitPath(), mIv_user_head);
        mIv_user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailActivity.this, ShowImageActivity.class);
                intent.putExtra("path", mUser.getHeadPortraitPath());
                intent.putExtra("type", "icon");
                startActivity(intent);
            }
        });
        //用户名
        mTv_user_name.setText(mUser.getUsername());
        //个性签名
        if (!TextUtils.isEmpty(mUser.getSignature())) {
            mTv_personalized_signature.setText(mUser.getSignature());
        }
        //用户性别
        iv_user_sex.setImageResource("1".equals(mUser.getSex()) ? R.drawable.sex_woman : R.drawable.sex_man);
        //注册时间
        stv_registration_date.setRightString(TimeUtil.format(mUser.getRegistrationDate()));

        //星座
        if (mUser.getConstellation() != null) {
            mStv_user_constellation.setRightString(mUser.getConstellation());
        }
        //年龄
        if (!TextUtils.isEmpty(mUser.getYearOfBirth())) {
            Calendar ca = Calendar.getInstance();
            int curYear = ca.get(Calendar.YEAR);//获取年份

            int age = curYear - Integer.parseInt(mUser.getYearOfBirth());
            mStv_user_age.setRightString(age + "");
        }
    }
}
