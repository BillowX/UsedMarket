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

        //融云初始化
        RongIM.init(this);

        /*//初始化ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)//设置当前线程的优先级
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//使用MD5对UIL进行加密命名
                .diskCacheSize(100 * 1024 * 1024)//50 Mb sd卡(本地)缓存的最大值
                .diskCacheFileCount(300)// 可以缓存的文件数量
                .tasksProcessingOrder(QueueProcessingType.LIFO)//后进先出
                .build();
        ImageLoader.getInstance().init(config);*/
    }
}
