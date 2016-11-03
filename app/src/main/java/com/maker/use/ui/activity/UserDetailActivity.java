package com.maker.use.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.supertextviewlibrary.SuperTextView;
import com.maker.use.R;
import com.maker.use.domain.User;
import com.maker.use.global.ConstentValue;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.manager.ActivityCollector;
import com.maker.use.ui.view.picker.ConstellationPicker;
import com.maker.use.ui.view.picker.ProfessorPicker;
import com.maker.use.utils.GlideUtils;
import com.maker.use.utils.GsonUtils;
import com.maker.use.utils.ImageCompressUtils;
import com.maker.use.utils.LoginUtils;
import com.maker.use.utils.MD5;
import com.maker.use.utils.SpUtil;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static com.maker.use.R.id.stv_user_age;
import static com.maker.use.R.id.stv_user_phone;

/**
 * 用户详情页
 * Created by XT on 2016/10/6.
 */
public class UserDetailActivity extends Activity implements View.OnClickListener, PopupWindow.OnDismissListener {

    private User mUser;
    private Calendar calendar = Calendar.getInstance();

    private ImageView mIv_user_head;
    private ImageView iv_user_sex;
    private TextView mTv_user_name;
    private TextView mTv_personalized_signature;
    private SuperTextView mStv_user_phone;
    private SuperTextView mStv_user_age;
    private SuperTextView mStv_user_blood;
    private SuperTextView mStv_user_constellation;
    private SuperTextView mStv_user_shippingAddress;
    private SuperTextView stv_registration_date;
    private PopupWindow pop;
    private SwipeRefreshLayout mRefresh_root;

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
        initPopupWindow();
    }

    private void initData() {
        mUser = (User) getIntent().getSerializableExtra("user");
    }

    private void initView() {
        mRefresh_root = (SwipeRefreshLayout) findViewById(R.id.refresh_root);
        mIv_user_head = (ImageView) findViewById(R.id.iv_user_head);
        iv_user_sex = (ImageView) findViewById(R.id.iv_user_sex);
        mTv_user_name = (TextView) findViewById(R.id.tv_user_name);
        mTv_personalized_signature = (TextView) findViewById(R.id.tv_personalized_signature);
        mStv_user_phone = (SuperTextView) findViewById(stv_user_phone);
        mStv_user_age = (SuperTextView) findViewById(stv_user_age);
        mStv_user_blood = (SuperTextView) findViewById(R.id.stv_user_blood);
        mStv_user_constellation = (SuperTextView) findViewById(R.id.stv_user_constellation);
        mStv_user_shippingAddress = (SuperTextView) findViewById(R.id.stv_user_shippingAddress);
        stv_registration_date = (SuperTextView) findViewById(R.id.stv_registration_date);
        mIv_user_head.setOnClickListener(this);
        mTv_personalized_signature.setOnClickListener(this);
        mStv_user_age.setOnClickListener(this);
        mStv_user_blood.setOnClickListener(this);
        mStv_user_constellation.setOnClickListener(this);
        mStv_user_shippingAddress.setOnClickListener(this);

        //设置刷新时动画的颜色，可以设置4个
        mRefresh_root.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mRefresh_root.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //本页面面刷新时，去根据用户ID重新获取用户信息，并保存
                LoginUtils.login(mUser.getUsername(), mUser.getPassword(), UserDetailActivity.this);
                String s = SpUtil.getString(ConstentValue.USER, "");
                mUser = GsonUtils.getGson().fromJson(s, User.class);
                //重新显示用户信息
                initViewData();
                mRefresh_root.setRefreshing(false);
                UIUtils.toast("刷新完成");
            }
        });
        initViewData();
    }

    private void initViewData() {
        //头像
        GlideUtils.setImg(this, UsedMarketURL.HEAD + mUser.getNarrowHeadPortraitPath(), mIv_user_head);
        //用户名
        mTv_user_name.setText(mUser.getUsername());
        //用户性别
        iv_user_sex.setImageResource("1".equals(mUser.getSex()) ? R.drawable.sex_woman : R.drawable.sex_man);
        //星座
        mStv_user_constellation.setRightString(mUser.getConstellation());
        //年龄
        Calendar ca = Calendar.getInstance();
        int curYear = ca.get(Calendar.YEAR);//获取年份
        int age = curYear - Integer.getInteger(mUser.getBirthday());
        mStv_user_age.setRightString(age + "");
        //血型
        mStv_user_blood.setRightString(mUser.getBloodType());
        //电话号码
        mStv_user_phone.setRightString(mUser.getPhone());
        //收货地址
        mStv_user_shippingAddress.setRightString(mUser.getShippingAddress());
        //注册时间
        stv_registration_date.setRightString(mUser.getRegistrationDate());
    }

    private void initPopupWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.popupwindow_userhead, null);

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        pop.setOnDismissListener(this);
        RelativeLayout rl_pop_null = (RelativeLayout) view
                .findViewById(R.id.third_popupwindow_layout_null);
        TextView tv_pop_quxiao = (TextView) view
                .findViewById(R.id.third_popupwindow_textView_quxiao);
        TextView tv_pop_chakan = (TextView) view
                .findViewById(R.id.third_popupwindow_textView_look);
        TextView tv_pop_change = (TextView) view
                .findViewById(R.id.third_popupwindow_textView_change);
        rl_pop_null.setOnClickListener(this);
        tv_pop_quxiao.setOnClickListener(this);
        tv_pop_chakan.setOnClickListener(this);
        tv_pop_change.setOnClickListener(this);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
    }

    //修改密码按钮
    public void changePassword(View view) {
        showChangePasswordDialog();
    }

    /**
     * 显示修改密码的对话框
     */
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

    /**
     * 显示popupwindow
     */
    private void showPopupWindow() {
        if (pop.isShowing()) {
            // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
            pop.dismiss();
        } else {
            // 显示窗口
            // pop.showAsDropDown(v);
            // 获取屏幕和PopupWindow的width和height
            pop.setAnimationStyle(R.style.MenuAnimationFade);
            pop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            pop.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
            ImageView iviv = (ImageView) findViewById(R.id.iviv);
            pop.showAsDropDown(iviv, 0, 0);

            WindowManager.LayoutParams lp = getWindow()
                    .getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_head:
                showPopupWindow();
                break;
            case R.id.third_popupwindow_textView_look://查看头像大图
                Intent intent = new Intent(UserDetailActivity.this, ShowImageActivity.class);
                intent.putExtra("path", mUser.getHeadPortraitPath());
                intent.putExtra("type", "icon");
                startActivity(intent);
                break;
            case R.id.third_popupwindow_layout_null://点击POP的无选项区域
                if (pop != null) {
                    pop.dismiss();
                }
                break;
            case R.id.third_popupwindow_textView_quxiao://取消按钮
                if (pop != null) {
                    pop.dismiss();
                }
                break;
            case R.id.third_popupwindow_textView_change://改变头像
                MultiImageSelector.create(UIUtils.getContext())
                        .showCamera(true) // 是否显示相机. 默认为显示
                        .single() // 单选模式
                        .start(UserDetailActivity.this, ConstentValue.REQUEST_IMAGE);

                if (pop != null) {
                    pop.dismiss();
                }
                break;
            case R.id.stv_user_age://修改年龄
                onYearMonthDayPicker();
                break;
            case R.id.stv_user_blood://修改血型
                onBloodTypePicker();
                break;
            case R.id.stv_user_constellation://修改星座
                onConstellationPicker();
                break;
            case R.id.stv_user_shippingAddress://修改星座
                startActivityForResult(new Intent(this, EditActivity.class), ConstentValue.EDIT_ACTIVITY);
                break;
        }
    }

    /**
     * 年龄选择
     */
    private void onYearMonthDayPicker() {
        DatePicker picker = new DatePicker(this);
        picker.setRange(1910, 2016);
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                //showToast(year + "-" + month + "-" + day);
                int yearOld = Integer.parseInt(year);
                HashMap<String, String> map = new HashMap<>();
                map.put("index", "birthday");
                map.put("futureValue", yearOld + "");
                upUserData(map);
            }
        });
        picker.show();
    }

    /**
     * 血型选择
     */
    private void onBloodTypePicker() {
        ProfessorPicker picker = new ProfessorPicker(this);
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopLineVisible(false);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setTextColor(0xFFFF0000, 0xFFCCCCCC);
        picker.setLineColor(0xFFEE0000);
        picker.setSelectedItem("UnKnow");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int position, String option) {
                HashMap<String, String> map = new HashMap<>();
                map.put("index", "bloodType");
                map.put("futureValue", option);
                upUserData(map);
            }
        });
        picker.show();
    }

    /**
     * 星座选择器
     */
    private void onConstellationPicker() {
        ConstellationPicker picker = new ConstellationPicker(this);
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopLineVisible(false);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setTextColor(0xFFFF0000, 0xFFCCCCCC);
        picker.setLineColor(0xFFEE0000);
        picker.setSelectedItem("射手");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int position, String option) {
                HashMap<String, String> map = new HashMap<>();
                map.put("index", "constellation");
                map.put("futureValue", option);
                upUserData(map);
            }
        });
        picker.show();

    }

    @Override
    public void onDismiss() {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstentValue.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                UIUtils.progressDialog(this);
                ArrayList<String> selectedPicture = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                final String str = selectedPicture.get(0);
                File headFile = ImageCompressUtils.getFile(UserDetailActivity.this, str);
                if (headFile != null) {
                    RequestParams params = new RequestParams(UsedMarketURL.CHANGE_HEAD);    // 网址
                    params.addBodyParameter("headPortrait", headFile);
                    params.addBodyParameter("userId", mUser.getUserId());
                    x.http().post(params, new Callback.CommonCallback<String>() {

                        @Override
                        public void onSuccess(String result) {
                            UIUtils.toast(result);
                            //如果修改成功了，则重新加载头像
                            GlideUtils.setImg(UserDetailActivity.this, UsedMarketURL.HEAD + mUser.getNarrowHeadPortraitPath(), mIv_user_head);
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
            }
        }

        if (requestCode == ConstentValue.EDIT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String center = data.getStringExtra("center");
                HashMap<String, String> map = new HashMap<>();
                map.put("index", "shippingAddress");
                map.put("futureValue", center);
                upUserData(map);
            }
        }
    }

    public void upUserData(Map<String, String> map) {
        RequestParams params = new RequestParams(UsedMarketURL.CHANGE_USER_INFO);    // 网址
        params.addBodyParameter("userId", mUser.getUserId());
        params.addBodyParameter("index", map.get("index"));
        params.addBodyParameter("futureValue", map.get("futureValue"));
        params.addBodyParameter("currentValue", "");
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    mUser = GsonUtils.getGson().fromJson(result, User.class);
                    initViewData();
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
            }
        });
    }

}
