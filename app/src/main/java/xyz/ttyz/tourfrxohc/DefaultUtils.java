package xyz.ttyz.tourfrxohc;

import java.util.HashSet;

import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;

public class DefaultUtils {
    public static String getIp () {
        if( BuildConfig.BUILD_TYPE.equals("release")){
            return "https://eevee.maihaoche.net/api";
        } else {
            return "https://emerald-u.maihaoche.net";
        }
    }
    public static void setCookie(HashSet<String> cookies) {
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "cookie", cookies);
    }

    public static HashSet<String> getCookie() {
        return SharedPreferenceUtil.getShareStringSet(ActivityManager.getInstance(), "cookie");
    }

    public static void removeCookie() {
        SharedPreferenceUtil.clear(ActivityManager.getInstance(), "cookie");
    }

}

