package xyz.ttyz.tourfrxohc;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashSet;

import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.tourfrxohc.models.UserModel;

public class DefaultUtils {
    public static void setCookie(HashSet<String> cookies){
        SharedPreferences.Editor config = ActivityManager.getInstance().getSharedPreferences("config", ActivityManager.getInstance().MODE_PRIVATE)
                .edit();
        config.putStringSet("cookie", cookies);
        config.apply();
    }
    public static HashSet<String> getCookie(){
        return (HashSet) ActivityManager.getInstance().getSharedPreferences("config",
                ActivityManager.getInstance().MODE_PRIVATE).getStringSet("cookie", null);
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
        setCookie(null);
        setUser(null);
    }

}
