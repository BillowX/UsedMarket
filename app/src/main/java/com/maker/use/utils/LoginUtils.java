package com.maker.use.utils;

import android.app.Activity;
import android.content.Intent;

import com.maker.use.domain.User;
import com.maker.use.global.ConstentValue;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.manager.ActivityCollector;
import com.maker.use.ui.activity.MainActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 登陆工具类
 * Created by XT on 2016/10/6.
 */

public class LoginUtils {

    public static onLoginListener mOnLoginListener;

    /**
     * 传入用户名和密码进行验证登陆
     *
     * @param username
     * @param password  MD5加密过的密码
     */
    public static void login(String username, String password, final Activity activity) {
        UIUtils.progressDialog(activity);
        RequestParams params = new RequestParams(UsedMarketURL.LOGIN);
        params.addBodyParameter("username", username);
        params.addBodyParameter("password", password);
//        params.addQueryStringParameter("username", username);
//        params.addQueryStringParameter("password", MD5.md5(password));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                //更新登陆状态
                SpUtil.putBoolean(ConstentValue.IS_LOGIN, false);
                if (!"登陆失败".equals(result)) {
                    User user = GsonUtils.getGson().fromJson(result, User.class);
                    //保存用户信息
                    SpUtil.putString(ConstentValue.USER, result);
                    //在融云注册该用户
                    IMKitUtils.getToken(user);

                    if (!(activity instanceof MainActivity)) {
                        ActivityCollector.finishAll();
                        Intent intent = new Intent(UIUtils.getContext(), MainActivity.class);
                        intent.putExtra("info", "login");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        UIUtils.getContext().startActivity(intent);
                    } else {
                        mOnLoginListener.onLogin(user);
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

    //登陆回调接口
    public interface onLoginListener {
        public void onLogin(User user);
    }
}
