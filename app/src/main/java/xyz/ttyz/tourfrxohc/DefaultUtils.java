package xyz.ttyz.tourfrxohc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashSet;

import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.mylibrary.protect.StringUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.tourfrxohc.models.UserModel;

public class DefaultUtils {
    public static final String key = "340475209711";
    public static final String secret = "S2022102818123462446803142";

    public static void setCookie(HashSet<String> cookies){
        SharedPreferences.Editor config = BaseApplication.ctx.getSharedPreferences("config", BaseApplication.ctx.MODE_PRIVATE)
                .edit();
        config.putStringSet("cookie", cookies);
        config.apply();
    }
    public static HashSet<String> getCookie(){
        return (HashSet) BaseApplication.ctx.getSharedPreferences("config",
                BaseApplication.ctx.MODE_PRIVATE).getStringSet("cookie", null);
    }
    public static void removeCookie(){
        SharedPreferences.Editor config = BaseApplication.ctx.getSharedPreferences("config", BaseApplication.ctx.MODE_PRIVATE)
                .edit();
        config.clear();
        config.apply();
    }
    public static void setUser(UserModel user) {
        if (user == null) {
            SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "user", null);
        } else
            SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "user", new Gson().toJson(user));

    }

    public static UserModel getUser() {
        String userStr = SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), "user");
        UserModel userModel =  new Gson().fromJson(userStr, UserModel.class);
        if(userModel == null){
            userModel = new UserModel();
        }
        return userModel;
    }

    public static void clearCache(){
        removeCookie();
        setUser(null);
    }



    //配置
//    private static String doorID = "1001";
//    private static String ip = "http://47.111.185.38:8001/";
    public static String defaultIP = "http://47.111.185.38:8001/";

    public static String getDoorID() {
        return SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), "doorID");
    }

    public static String getIp() {
        return getIp(BaseApplication.ctx);
    }
    public static String getIp(Context context) {
        String ip = SharedPreferenceUtil.getShareString(context, "ip");
        if(StringUtil.safeString(ip).isEmpty()){
            ip = defaultIP;
        }
        return ip;
    }

    public static void setDoorID(String doorID) {
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "doorID", doorID);
    }

    public static void setIp(String ip) {
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "ip", ip);
    }
}
