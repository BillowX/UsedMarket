package com.maker.use.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;

import com.avos.avoscloud.AVOSCloud;
import com.maker.use.manager.CustomUserProvider;
import com.maker.use.utils.SpUtil;

import org.xutils.x;

import cn.leancloud.chatkit.LCChatKit;

/**
 * 自定义Application,进行全局初始化
 * Created by XT on 2016/9/24.
 */

public class USEApplication extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    private final String APP_ID = "NH5LATNQKpY9f2lUsVrJGLsG-gzGzoHsz";
    private final String APP_KEY = "FrTzo5Y6jEpgestevq1TiGWl";

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        this.context = getApplicationContext();
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        this.mainThreadId = Process.myTid();

        SpUtil.putBoolean(ConstentValue.IS_LOGIN, false);

        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        AVOSCloud.setDebugLogEnabled(true);
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
    }
}
