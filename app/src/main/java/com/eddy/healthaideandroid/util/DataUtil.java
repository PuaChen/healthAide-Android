package com.eddy.healthaideandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created with Android Studio
 * Author:Chen·ZD
 * Date:2019/3/8
 */

public class DataUtil {

    /*
       读取或者保存数据到手机本地存储上
    */
    public static String readSpDataByString(Context context, String dirName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(dirName, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static int readSpDataByInt(Context context, String dirName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(dirName, Context.MODE_PRIVATE);
        return preferences.getInt(key, -1);
    }

    public static void saveDataByString(Context context, String dirName, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(dirName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveDataByInt(Context context, String dirName, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(dirName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


}
