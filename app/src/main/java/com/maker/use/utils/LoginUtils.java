package com.maker.use.utils;

import android.app.Activity;
import android.content.Intent;

import com.maker.use.domain.User;
import com.maker.use.global.ConstentValue;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.manager.ActivityCollector;
import com.maker.use.ui.activity.LoginActivity;
import com.maker.use.ui.activity.MainActivity;
import com.maker.use.ui.activity.UserDetailActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 登陆工具类
 * Created by XT on 2016/10/6.
 */

public class LoginUtils {

    private static onLoginListener mOnLoginListener;
    private static onLoginStatusChangeListener mOnLoginStatusChangeListener;

    /**
     * 传入用户名和密码进行验证登陆
     *
     * @param username
     * @param password MD5加密过的密码
     */
    public static void login(String username, String password, final Activity activity) {
        UIUtils.progressDialog(activity);
        RequestParams params = new RequestParams(UsedMarketURL.LOGIN);
        params.addBodyParameter("username", username);
        params.addBodyParameter("password", password);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                //更新登陆状态
                SpUtil.putBoolean(ConstentValue.IS_LOGIN, false);
                if (!"登录失败".equals(result)) {
                    User user = GsonUtils.getGson().fromJson(result, User.class);
                    //保存用户信息
                    SpUtil.putString(ConstentValue.USER, result);
                    //在融云注册该用户
                    IMKitUtils.getToken(user);

                    if (activity instanceof MainActivity) {
                        //刚进来应用时用保存的用户名和密码登陆的
                        mOnLoginListener.onLogin(user);
                        mOnLoginStatusChangeListener.onLogin(user);
                    } else if (activity instanceof LoginActivity) {
                        //在登陆页点击登陆，但是最后的逻辑是，用保存的用户信息去登陆，所以不更新登陆状态
                        ActivityCollector.finishAll();
                        Intent intent = new Intent(UIUtils.getContext(), MainActivity.class);
                        intent.putExtra("info", "login");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        UIUtils.getContext().startActivity(intent);
                    } else if (activity instanceof UserDetailActivity) {
                        //更新登陆状态
                        SpUtil.putBoolean(ConstentValue.IS_LOGIN, true);
                        //什么都不做
                    }
                } else {
                    UIUtils.toast("用户名或密码错误");
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

    public static void setOnLoginListener(onLoginListener listener) {
        mOnLoginListener = listener;
    }

    public static void setOnLoginStatusChangeListener(onLoginStatusChangeListener listener) {
        mOnLoginStatusChangeListener = listener;
    }

    //登陆回调接口
    public interface onLoginListener {
        public void onLogin(User user);
    }

    public interface onLoginStatusChangeListener {
        void onLogin(User user);
    }
}
