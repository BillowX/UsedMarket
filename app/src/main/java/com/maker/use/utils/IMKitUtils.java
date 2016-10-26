package com.maker.use.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.maker.use.domain.User;
import com.maker.use.global.UsedMarketURL;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Random;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by XISEVEN on 2016/10/17.
 */

public class IMKitUtils {

    private static final String RY_APP_KEY = "6tnym1brnmkc7";
    private static final String RY_APP_SECRET = "Xc51aOwNi5SM";
    private static String token;
    private static User mUser;

    private static RequestParams addHeader(RequestParams params) {
        Random r = new Random();
        String Nonce = (r.nextInt(10000) + 10000) + "";
        String Timestamp = (System.currentTimeMillis() / 1000) + "";
        params.addHeader("App-Key", RY_APP_KEY);
        params.addHeader("Nonce", Nonce);
        params.addHeader("Timestamp", Timestamp);
//        params.addHeader("Signature", MD5_test.encryptToSHA(RY_APP_SECRET + Nonce + Timestamp));
        params.addHeader("Signature", MD5_test.SHA1(RY_APP_SECRET + Nonce + Timestamp));
        return params;
    }

    public static String getToken(User user) {
        mUser = user;
        RequestParams params = new RequestParams("https://api.cn.ronghub.com/user/getToken.json");
        addHeader(params);

        params.addBodyParameter("userId", user.getUserId());
        params.addBodyParameter("name", user.getUsername());
        params.addBodyParameter("portraitUri", UsedMarketURL.HEAD + user.getHeadPortrait());

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("token", arg0.toString());
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String s) {
                Log.e("token", "success");
                Log.e("token", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    token = (String) jsonObject.get("token");
                    new IMKitUtils().connect(token);
                    Log.e("token", token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return token;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context context
     * @return 进程号
     */
    private static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public void connect(String token) {

        if (UIUtils.getContext().getApplicationInfo().packageName.equals(getCurProcessName(UIUtils.getContext().getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.d("token_connect", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {

                    Log.d("token_connect", "--onSuccess" + userid);
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid, mUser.getUsername(), Uri.parse(UsedMarketURL.HEAD + mUser.getHeadPortrait())));
                    }
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("token_connect", "--onError" + errorCode);
                }
            });
        }
    }
}
