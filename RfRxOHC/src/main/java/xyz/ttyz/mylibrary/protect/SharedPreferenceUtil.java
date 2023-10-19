package xyz.ttyz.mylibrary.protect;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;

import xyz.ttyz.mylibrary.method.LocalSaveConfig;

/**
 * SP相关工具类
 */
public class SharedPreferenceUtil {
    public static void setShareBool(Context c, String key, boolean value) {
        if(c == null){
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    /**
     * 存储String
     *
     * @param key   key
     * @param value 数值
     */
    public static void setShareString(Context c, String key, String value) {
        if(c == null){
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, StringUtil.safeString(value));
        editor.apply();
    }

    public static void setShareString(Context c, String key, HashSet<String> value) {
        if(c == null){
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    /**
     * 存储String
     *
     * @param key   key
     * @param value 数值
     */
    public static void setCacheShareString(Context c, String key, String value) {
        if(c == null){
            return;
        }
        setShareString(c, key, value);
        //为清除缓存做准备
        LocalSaveConfig.addSaveKey(c, key);
    }

    /**
     * 取出String
     *
     * @param key key
     * @return String
     */
    public static String getShareString(Context c, String key) {
        if(c == null || key == null){
            return "";
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getString(key, "");
    }
    public static boolean getShareBool(Context c, String key) {
        if(c == null || key == null){
            return false;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean(key, false);
    }

    public static HashSet<String> getShareStringSet(Context c, String key) {
        if(c == null || key == null){
            return null;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return (HashSet<String>) sharedPreferences.getStringSet(key, null);
    }

    public static void clear(Context c, String key){
        if(c == null || key == null){
            return ;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        sharedPreferences.edit().clear().apply();
    }
}