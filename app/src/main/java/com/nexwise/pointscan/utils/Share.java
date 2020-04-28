package com.nexwise.pointscan.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sp:本地存储类
 * Created by xsfon 2019/8/24.
 */
public class Share {
    private static final String SHARP_NAME = "nexwise";
    private static final String DEF_STRING = "";
    private static SharedPreferences sp;

    /**
     * 实例化SharedPreferences
     *
     * @param context 上下文
     */
    private static void instanc(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SHARP_NAME, Context.MODE_PRIVATE);
        }
    }

    public static SharedPreferences getSp(Context context) {
        instanc(context);
        return sp;
    }


    /**
     * @param key     键
     * @param value   值
     * @param context 环境
     */
    public static void putString(String key, String value, Context context) {
        instanc(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * @param key     键
     * @param context 环境
     * @return 键
     */
    public static String getString(String key, Context context) {
        instanc(context);
        return sp.getString(key, DEF_STRING);
    }

    /**
     * 自定义缺省值的方式获取数据
     *
     * @param context      un
     * @param key          un
     * @param defaultValue 缺省值
     * @return un
     */
    public static String getString(Context context, String key, String defaultValue) {
        instanc(context);
        return sp.getString(key, defaultValue);
    }

    /**
     * @param key     键
     * @param value   值
     * @param context 环境
     */
    public static void putBoolean(String key, boolean value, Context context) {
        instanc(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * @param key     键
     * @param context 环境
     * @return boolean
     */
    public static boolean getBoolean(String key, Context context) {
        instanc(context);
        return sp.getBoolean(key, false);
    }

}
