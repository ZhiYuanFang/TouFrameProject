package xyz.ttyz.tourfrxohc;

import androidx.databinding.Bindable;

import com.google.gson.Gson;

import java.util.HashSet;

import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.tourfrxohc.models.LocationModel;

public class DefaultUtils {
    public static LocationModel locationModel;//内部页面绘制使用，业务逻辑不要直接用这个

    public static void setLocationModel(LocationModel model){
        locationModel = model;
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "location", new Gson().toJson(model));

    }
    public static LocationModel getLocalLocationModel(){
        if (locationModel == null)
             locationModel = new Gson().fromJson(SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), "location"), LocationModel.class);
        // 初始化
        if(locationModel == null){
            locationModel = new LocationModel(-1, -1, "请选择", "仓库");
        }
        return locationModel;
    }
    public static String getIp () {
        if(isRelease()){
            return "https://emerald.maihaoche.com";
        } else {
            return "https://emerald-u.maihaoche.net";
        }
    }

    public static boolean isRelease () {
        return BuildConfig.BUILD_TYPE.equals("release");
    }

    private static HashSet<String> cookie;
    public static void setCookie(HashSet<String> cookies) {
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "cookie", cookies);
        cookie = cookies;
    }

    public static HashSet<String> getCookie() {
        if(cookie == null)
            cookie = SharedPreferenceUtil.getShareStringSet(ActivityManager.getInstance(), "cookie");
        return cookie;
    }

    public static void removeCookie() {
        cookie = null;
        SharedPreferenceUtil.clear(ActivityManager.getInstance(), "cookie");
    }

}

