package com.maker.use.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 单例模式的Gson工具类
 * Created by XT on 2016/10/25.
 */

public class GsonUtils {
    private static Gson gson = null;

    public static Gson getGson() {
        if (gson == null) {
            synchronized (GsonUtils.class) {
                if (gson == null) {
                    gson = new GsonBuilder()
                            .setDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .create();
                }
            }
        }
        return gson;
    }



}
