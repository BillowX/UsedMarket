package com.maker.use.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.maker.use.global.USEApplication;

/**
 * UI工具类
 * Created by XT on 2016/9/24.
 */

public class UIUtils {
    private static Toast toast;

    /**
     * 获取应用上下文对象
     *
     * @return
     */
    public static Context getContext() {
        return USEApplication.getContext();
    }

    /**
     * 获取Handler对象
     *
     * @return
     */
    public static Handler getHandler() {
        return USEApplication.getHandler();
    }

    /**
     * 获取主线程ID
     *
     * @return
     */
    public static int getMainThreadId() {
        return USEApplication.getMainThreadId();
    }

    /**
     * 根据ID获取字符串
     *
     * @param id
     * @return
     */
    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    /**
     * 根据ID获取字符串数组
     *
     * @param id
     * @return
     */
    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    /**
     * 根据ID获取整数数组
     *
     * @param id
     * @return
     */
    public static int[] getIntArray(int id) {
        return getContext().getResources().getIntArray(id);
    }

    /**
     * 根据ID获取图片
     *
     * @param id
     * @return
     */
    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    /**
     * 根据ID获取颜色值
     *
     * @param id
     * @return
     */
    public static int getColor(int id) {
        return getContext().getResources().getColor(id);
    }

    /**
     * 根据ID获取颜色的状态选择器
     *
     * @param id
     * @return
     */
    public static ColorStateList getColorStateList(int id) {
        return getContext().getResources().getColorStateList(id);
    }

    /**
     * 根据ID获取尺寸的具体像素值
     *
     * @param id
     * @return
     */
    public static int getDimen(int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }

    /**
     * 像素密度转换成像素
     *
     * @param dip
     * @return
     */
    public static int dip2px(float dip) {
        //获取设备密度
        Context context = getContext();
        float density = getContext().getResources().getDisplayMetrics().density;
        //四舍五入
        return (int) (dip * density + 0.5f);
    }

    /**
     * 像素转换成像素密度
     *
     * @param px
     * @return
     */
    public static float px2dip(int px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return px / density;
    }

    /**
     * 根据ID加载布局文件
     *
     * @param id
     * @return
     */
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    /**
     * 判断是否运行在主线程（UI线程）
     *
     * @return
     */
    public static boolean isRunOnMainThread() {
        return Process.myTid() == getMainThreadId();
    }

    /**
     * 使线程运行在主线程
     *
     * @param r
     */
    public static void runOnMainThread(Runnable r) {
        if (isRunOnMainThread()) {
            //如果传进来的线程是运行在主线程的，直接运行
            r.run();
        } else {
            //如果是子线程, 借助handler让其运行在主线程
            getHandler().post(r);
        }
    }

    /**
     * 显示吐司
     *
     * @param msg
     */
    public static void toast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
        }
        //将text文本设置给吐司
        toast.setText(msg);
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }

    /**
     * 显示SnackBar
     */
    public static void snackBar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    /**
     * 隐藏view对象
     *
     * @param view
     */
    public static void setViewGone(View... view) {
        for (int i = 0; i < view.length; i++) {
            if (view[i] != null) {
                view[i].setVisibility(View.GONE);
            }
        }
    }
}
