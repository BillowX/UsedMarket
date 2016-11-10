package com.maker.use.utils;

import com.google.gson.Gson;

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
                    gson = new Gson();
                }
            }
        }
        return gson;
    }


}
