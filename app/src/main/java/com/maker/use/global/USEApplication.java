package com.maker.use.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;

import com.maker.use.utils.SpUtil;

import org.xutils.x;

import io.rong.imkit.RongIM;

/**
 * 自定义Application,进行全局初始化
 * Created by XT on 2016/9/24.
 */

public class USEApplication extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

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

        RongIM.init(this);
    }
}
