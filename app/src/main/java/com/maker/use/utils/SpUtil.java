package com.maker.use.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    /**
     * 在sp中存储一个boolean对象的值
     *
     * @param key
     * @param value
     */
    public static void putBoolean(String key, boolean value) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences("config",
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 在sp中存储一个String对象的值ֵ
     *
     * @param key
     * @param value
     */
    public static void putString(String key, String value) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences("config",
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 在sp中存储一个int对象的值ֵ
     *
     * @param key
     * @param value
     */
    public static void putInt(String key, int value) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences("config",
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    /**
     * 在sp中获取相应keyֵ值的boolean值ֵ
     *
     * @param key
     * @param defValue
     * @return ֵ
     */
    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 在sp中获取相应keyֵ值的String值ֵ
     *
     * @param key
     * @param defValue
     * @return ֵ
     */
    public static String getString(String key, String defValue) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    /**
     * 在sp中获取相应keyֵ值的int值ֵ
     *
     * @param key
     * @param defValue
     * @return ֵ
     */
    public static int getInt(String key, int defValue) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }
}
