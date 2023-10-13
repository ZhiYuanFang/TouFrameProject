package xyz.ttyz.tourfrxohc;

import java.util.HashSet;

import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;

public class DefaultUtils {
    public static String token;

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

